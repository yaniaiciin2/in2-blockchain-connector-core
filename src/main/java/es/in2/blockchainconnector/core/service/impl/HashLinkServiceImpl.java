package es.in2.blockchainconnector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.exception.HashLinkException;
import es.in2.blockchainconnector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchainconnector.core.service.HashLinkService;
import es.in2.blockchainconnector.core.utils.ApplicationUtils;
import es.in2.blockchainconnector.core.utils.BlockchainConnectorUtils;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.BrokerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashLinkServiceImpl implements HashLinkService {

	private final BrokerProperties brokerProperties;
	private final ApplicationUtils applicationUtils;
	private final ObjectMapper objectMapper;

	@Override
	public Mono<String> createHashLink(String id, String data) {
		return generateHashFromString(data).map(generatedHash -> {
			log.debug(" > Creating hashlink...");
			// Build dynamic URL by Orion-LD Use Case
			String orionLdEntitiesUrl = brokerProperties.externalDomain() + brokerProperties.paths().entities();
			// Create Hashlink
			return orionLdEntitiesUrl + "/" + id + BlockchainConnectorUtils.HASHLINK_PARAMETER + generatedHash;
		});
	}

	@Override
	public Mono<String> resolveHashlink(String dataLocation) {
		log.debug(" > Resolving hashlink...");
		return executeHashlinkRequest(dataLocation)
				.flatMap(retrievedEntity -> verifyHashlink(dataLocation, retrievedEntity).thenReturn(retrievedEntity))
				.doOnSuccess(retrievedEntity -> log.debug(" > Hashlink resolved."));
	}


	@Override
	public Mono<Boolean> compareHashLinksFromEntities(String retrievedEntity, String originOffChainEntity) {
		return generateHashFromString(retrievedEntity)
				.flatMap(retrievedEntityHash -> generateHashFromString(originOffChainEntity)
						.map(retrievedEntityHash::equals)
				);
	}


	private Mono<String> executeHashlinkRequest(String dataLocation) {
		return Mono.defer(() -> {
			String offChainEntityOriginUrl = extractOffChainEntityOriginUrl(dataLocation);
			return Mono.fromSupplier(() -> applicationUtils.getRequest(offChainEntityOriginUrl));
		});
	}

	private Mono<Void> verifyHashlink(String dataLocation, String originOffChainEntity) {
		return extractHashLink(dataLocation)
				.flatMap(originEntityHash -> generateHashFromString(originOffChainEntity)
						.flatMap(retrievedEntityHash -> {
							log.debug(" > Origin entity hash: " + originEntityHash);
							log.debug(" > Retrieved entity hash: " + retrievedEntityHash);
							if (!retrievedEntityHash.equals(originEntityHash)) {
								return Mono.error(new InvalidHashlinkComparisonException(
										"Invalid hash: Origin entity hash is different than Retrieved entity"));
							} else {
								return Mono.empty();
							}
						})
				)
				.then();
	}


	// the string to hash is a json-object. Therefor, it needs preprocessing to produce reliable results, since json does
	// not guarantee the order of properties.
	private Mono<String> generateHashFromString(String jsonData) {
		return Mono.fromSupplier(() -> {
			try {
				log.info("Create hash from {}", jsonData);
				return applicationUtils.calculateSHA256Hash(createOrderedString(jsonData));
			} catch (NoSuchAlgorithmException e) {
				throw new HashLinkException("Error creating Hashlink");
			}
		});
	}


	private String createOrderedString(String jsonData) {
		try {
			Object jsonObject = objectMapper.readerFor(Object.class).readValue(jsonData);
			return objectMapper.writer().writeValueAsString(jsonObject);
		} catch (JsonProcessingException e) {
			log.warn("Was not able to parse and order the json string {}. Will continue with the original string.",
					jsonData, e);
			return jsonData;
		}
	}

	private static String extractOffChainEntityOriginUrl(String url) {
		Pattern pattern = Pattern.compile("^[^?]+");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group();
		} else {
			throw new IllegalArgumentException("Invalid URL");
		}
	}

	@Override
	public Mono<String> extractHashLink(String url) {
		return Mono.fromSupplier(() -> {
			Pattern pattern = Pattern.compile("hl=([^&]*)");
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				return matcher.group(1);
			} else {
				throw new IllegalArgumentException("Invalid Path");
			}
		});
	}


}