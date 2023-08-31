package es.in2.dome.blockchain.connector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.configuration.ContextBrokerConfigApi;
import es.in2.dome.blockchain.connector.exception.InvalidHashlinkComparisonException;
import es.in2.dome.blockchain.connector.service.DomeEventService;
import es.in2.dome.blockchain.connector.service.HashLinkResolutionService;
import es.in2.dome.blockchain.connector.utils.ApplicationUtils;
import es.in2.dome.blockchain.connector.utils.BlockchainConnectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashLinkResolutionServiceImpl implements HashLinkResolutionService {

    private final ApplicationUtils applicationUtils;
    private final DomeEventService domeEventService;
    private final ContextBrokerConfigApi contextBrokerConfigApi;
    private final ObjectMapper objectMapper;
    @Override
    public void resolveHashlink(String datalocation) throws JsonProcessingException, InvalidHashlinkComparisonException {
        String baseUrl = extractBaseUrl(datalocation);
        String hashLink = extractHashLink(datalocation);

        log.debug("Retrieving data info...");
        String response = applicationUtils.getRequest(baseUrl);
        JsonNode rootNode = objectMapper.readTree(response);
        String entityId = rootNode.get(BlockchainConnectorUtils.ID_FIELD).asText();
        String hashlinkRetrieved = domeEventService.createHash(response, entityId);

        boolean sameHashlink = hashlinkRetrieved.equals(hashLink);
        if (!sameHashlink) {
            log.debug("Different hashlinks");
            throw new InvalidHashlinkComparisonException("Invalid hashlink: Different than origin");
        } else {
           log.debug("Same hashlinks");
           log.debug(contextBrokerConfigApi.getEntitiesUrl());
           applicationUtils.postRequest(contextBrokerConfigApi.getEntitiesUrl(), response);
           log.debug("Uploaded entity to Orion I/F");
        }
    }



    private static String extractBaseUrl(String url) {
        Pattern pattern = Pattern.compile("^[^?]+");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        }

        return url;
    }


    private static String extractHashLink(String url) {
        Pattern pattern = Pattern.compile("hl=([^&]*)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}