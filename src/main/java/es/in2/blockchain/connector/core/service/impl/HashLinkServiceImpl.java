package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.exception.HashLinkException;
import es.in2.blockchain.connector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
	public String createHashLink(String id, String data) {
		log.debug(" > Creating hashlink...");
		// Generate hash from data
		String generatedHash = generateHashFromString(data);
		// Build dynamic URL by Orion-LD Use Case
		String orionLdEntitiesUrl = brokerProperties.externalDomain() + brokerProperties.paths().entities();
		// Create Hashlink
		return orionLdEntitiesUrl + "/" + id + BlockchainConnectorUtils.HASHLINK_PARAMETER + generatedHash;
	}

	@Override
	public String resolveHashlink(String dataLocation) {
		log.debug(" > Resolving hashlink...");
		// execute hashlink request to get entity from origin off-chain
		String retrievedEntity = executeHashlinkRequest(dataLocation);
		// verify entity with hashlink
		verifyHashlink(dataLocation, retrievedEntity);
		log.debug(" > Hashlink resolved.");
		return retrievedEntity;
	}

	@Override
	public boolean compareHashLinksFromEntities(String retrievedEntity, String originOffChainEntity) {
		String originEntityHash = generateHashFromString(retrievedEntity);
		log.debug(" > Origin entity hash: " + originEntityHash);
		String retrievedEntityHash = generateHashFromString(originOffChainEntity);
		log.debug(" > Retrieved entity hash: " + retrievedEntityHash);
		return retrievedEntityHash.equals(originEntityHash);
	}

	private String executeHashlinkRequest(String dataLocation) {
		String offChainEntityOriginUrl = extractOffChainEntityOriginUrl(dataLocation);
		return applicationUtils.getRequest(offChainEntityOriginUrl);
	}

	private void verifyHashlink(String dataLocation, String originOffChaiEntity) {
		String originEntityHash = extractHashLink(dataLocation);
		log.debug(" > Origin entity hash: " + originEntityHash);
		String retrievedEntityHash = generateHashFromString(originOffChaiEntity);
		log.debug(" > Retrieved entity hash: " + retrievedEntityHash);
		if (!retrievedEntityHash.equals(originEntityHash)) {
			throw new InvalidHashlinkComparisonException(
					"Invalid hash: Origin entity hash is different than Retrieved entity");
		}
	}

	// the string to hash is a json-object. Therefor, it needs preprocessing to produce reliable results, since json does
	// not guarantee the order of properties.
	private String generateHashFromString(String jsonData) {
		try {
			log.info("Create hash from {}", jsonData);
			return applicationUtils.calculateSHA256Hash(createOrderedString(jsonData));
		} catch (NoSuchAlgorithmException e) {
			throw new HashLinkException("Error creating Hashlink");
		}
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
	public String extractHashLink(String url) {
		Pattern pattern = Pattern.compile("hl=([^&]*)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			throw new IllegalArgumentException("Invalid Path");
		}
	}

}