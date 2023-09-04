package es.in2.dome.blockchainconnector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchainconnector.core.exception.HashLinkException;
import es.in2.dome.blockchainconnector.core.integration.orionld.configuration.OrionLdApiConfig;
import es.in2.dome.blockchainconnector.core.exception.InvalidHashlinkComparisonException;
import es.in2.dome.blockchainconnector.core.service.DomeEventService;
import es.in2.dome.blockchainconnector.core.service.HashLinkService;
import es.in2.dome.blockchainconnector.core.utils.ApplicationUtils;
import es.in2.dome.blockchainconnector.core.utils.BlockchainConnectorUtils;
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

    private final ApplicationUtils applicationUtils;
    private final OrionLdApiConfig orionLdApiConfig;

    @Override
    public void resolveHashlink(String dataLocation) {

        String baseUrl = extractBaseUrl(dataLocation);
        String hashFromEntityOrigin = extractHashLink(dataLocation);

        log.debug("Retrieving data info...");
        // Retrieve data from origin Orion-LD
        String response = applicationUtils.getRequest(baseUrl);
        log.debug("Data retrieved: " + response);

        // Create hash from retrieved entity
        String hashFromEntityRetrieved = createHashFromEntity(response);

        // Compare hashes
        if(hashFromEntityRetrieved.equals(hashFromEntityOrigin)){
            log.debug("Same hashes");
            // Publish retrieved entity to Orion-LD
            applicationUtils.postRequest(orionLdApiConfig.getEntitiesUrl(), response);
            log.debug("Entity published to Orion I/F successfully");
        } else {
            throw new InvalidHashlinkComparisonException("Invalid hash: Origin entity has is different than retrieved entity");
        }

    }

    private static String extractBaseUrl(String url) {
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

    @Override
    public String createHashLink(String id, String data) {
        String resourceHash = createHashFromEntity(data);
        return orionLdApiConfig.getEntitiesUrl() + "/" + id
                + BlockchainConnectorUtils.HASHLINK_PARAMETER + resourceHash;
    }

    private String createHashFromEntity(String entityData) {
        try {
            return applicationUtils.calculateSHA256Hash(entityData);
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
        }
    }

}