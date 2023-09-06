package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.exception.HashLinkException;
import es.in2.blockchain.connector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils;
import es.in2.blockchain.connector.integration.orionld.configuration.OrionLdProperties;
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

    private final OrionLdProperties orionLdProperties;
    private final ApplicationUtils applicationUtils;

    @Override
    public String createHashLink(String id, String data) {
        String resourceHash = createHashFromEntity(data);
        String orionLdEntitiesUrl = orionLdProperties.getOrionLdDomain() + orionLdProperties.getOrionLdPathEntities();
        return orionLdEntitiesUrl + "/" + id + BlockchainConnectorUtils.HASHLINK_PARAMETER + resourceHash;
    }

    @Override
    public String resolveHashlink(String dataLocation) {
        // execute hashlink request to get entity from origin off-chain
        String retrievedEntity = executeHashlinkRequest(dataLocation);
        // verify entity with hashlink
        verifyHashlink(dataLocation, retrievedEntity);
        return retrievedEntity;
    }

    private String executeHashlinkRequest(String dataLocation) {
        log.debug("Retrieving data info...");
        String offChainEntityOriginUrl = extractOffChainEntityOriginUrl(dataLocation);
        String response = applicationUtils.getRequest(offChainEntityOriginUrl);
        log.debug("Data retrieved: " + response);
        return response;
    }

    private void verifyHashlink(String dataLocation, String originOffChaiEntity) {
        String originEntityHash = extractHashLink(dataLocation);
        String retrievedEntityHash = createHashFromEntity(originOffChaiEntity);
        if (!retrievedEntityHash.equals(originEntityHash)) {
            throw new InvalidHashlinkComparisonException("Invalid hash: Origin entity hash is different than Retrieved entity");
        }
    }

    private String createHashFromEntity(String entityData) {
        try {
            return applicationUtils.calculateSHA256Hash(entityData);
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
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

    private static String extractHashLink(String url) {
        Pattern pattern = Pattern.compile("hl=([^&]*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("Invalid Path");
        }
    }

}
