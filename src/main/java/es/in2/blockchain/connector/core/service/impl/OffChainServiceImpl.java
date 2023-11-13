package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.OffChainService;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffChainServiceImpl implements OffChainService {

	private final ObjectMapper objectMapper;
	private final HashLinkService hashLinkService;
	private final ApplicationUtils applicationUtils;
	private final BrokerProperties brokerProperties;

	@Override
	public Mono<Void> retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO) {
		return Mono.defer(() -> {
			log.info(">>> Retrieving and publishing entity to off-chain...");
			String dataLocation = blockchainNodeNotificationDTO.getDataLocation();
			log.debug(" > Data Location: {}", dataLocation);

			return Mono.fromCallable(() -> {
				String retrievedEntity = String.valueOf(hashLinkService.resolveHashlink(dataLocation));
				try {
					String entityId = getIdFromOrionLdEntity(retrievedEntity);
					String existingEntity = retrieveExistingOrionLdEntity(entityId);
					log.debug("> Entity exists");
					compareAndPublishEntities(existingEntity, retrievedEntity).subscribe(); // Suscribe para que se ejecute
				} catch (NoSuchElementException e) {
					publishEntityToDestinationOffChain(retrievedEntity);
					log.info("  > Entity published to off-chain");
				}
				return null;
			}).then();
		});
	}


	private String buildOrionLdEntityUrl(String entityId) {
		return brokerProperties.internalDomain() + brokerProperties.paths().entities() + "/" + entityId;
	}

	private String buildOrionLdUpdateEntityUrl() {
		return brokerProperties.internalDomain() + brokerProperties.paths().entities();
	}

	private String getIdFromOrionLdEntity(String jsonString) {
		try {
			JsonNode jsonNode = objectMapper.reader().readTree(jsonString);
			JsonNode idNode = jsonNode.get("id");
			if (idNode != null) {
				return idNode.asText();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String retrieveExistingOrionLdEntity(String entityId) {
		String orionLdEntitiesUrl = buildOrionLdEntityUrl(entityId);
		return applicationUtils.getRequest(orionLdEntitiesUrl);
	}

	private void publishEntityToDestinationOffChain(String retrievedEntity) {
		String orionLdEntitiesUrl = brokerProperties.internalDomain() + brokerProperties.paths().entities();
		log.debug(" > Publishing entity to: {}", orionLdEntitiesUrl);
		log.debug(" > Entity to publish: {}", retrievedEntity);
		applicationUtils.postRequest(orionLdEntitiesUrl, retrievedEntity);
	}

	private Mono<Boolean> areEntitiesEqual(String retrievedEntity, String existingEntity) {
		return hashLinkService.compareHashLinksFromEntities(retrievedEntity, existingEntity);
	}


	private Mono<Void> compareAndPublishEntities(String existingEntity, String retrievedEntity) {
		return areEntitiesEqual(retrievedEntity, existingEntity)
				.flatMap(isTrue -> {
					if (Boolean.TRUE.equals(isTrue)) {
						log.info("> Same entities. No changes.");
						return Mono.empty();
					} else {
						log.debug("> Entities not equal");
						String retrievedEntityUrl = buildOrionLdUpdateEntityUrl();
						return applicationUtils.patchRequest(retrievedEntityUrl, retrievedEntity)
								.doOnSuccess(result -> log.info(" > Entity updated to off-chain"))
								.doOnError(error -> log.error("Error updating entity to off-chain", error))
								.then();
					}
				});
	}


}