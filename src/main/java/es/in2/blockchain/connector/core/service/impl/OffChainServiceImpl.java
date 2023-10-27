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

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffChainServiceImpl implements OffChainService {

    private final HashLinkService hashLinkService;
    private final ApplicationUtils applicationUtils;
    private final BrokerProperties brokerProperties;

    @Override
    public void retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO) {
        log.info(">>> Retrieving and publishing entity to off-chain...");
        String dataLocation = blockchainNodeNotificationDTO.getDataLocation();
        log.debug(" > Data Location: {}", dataLocation);
        String retrievedEntity = hashLinkService.resolveHashlink(dataLocation);
        try{
            String entityId = getIdFromOrionLdEntity(retrievedEntity);
            String existingEntity = retrieveExistingOrionLdEntity(entityId);
            log.debug("> Entity exists");
            compareAndPublishEntities(existingEntity, retrievedEntity);
        } catch(NoSuchElementException e) {
            publishEntityToDestinationOffChain(retrievedEntity);
            log.info("  > Entity published to off-chain");
        }
    }

    private String buildOrionLdEntityUrl(String entityId) {
        return brokerProperties.internalDomain() + brokerProperties.paths().entities() + "/" + entityId;
    }

    private String buildOrionLdUpdateEntityUrl() {
        return brokerProperties.internalDomain() + brokerProperties.paths().entities();
    }

    private String getIdFromOrionLdEntity(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
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

    private boolean areEntitiesEqual(String retrievedEntity, String existingEntity) {
        return hashLinkService.compareHashLinksFromEntities(retrievedEntity, existingEntity);
    }

    private void compareAndPublishEntities(String existingEntity, String retrievedEntity) {
        if(!areEntitiesEqual(retrievedEntity, existingEntity)) {
            log.debug("> Entities not equal");
            String retrievedEntityUrl = buildOrionLdUpdateEntityUrl();
            applicationUtils.patchRequest(retrievedEntityUrl, retrievedEntity);
            log.info("  > Entity updated to off-chain");
        } else {
            log.info("> Same entities. No changes.");
        }
    }

}