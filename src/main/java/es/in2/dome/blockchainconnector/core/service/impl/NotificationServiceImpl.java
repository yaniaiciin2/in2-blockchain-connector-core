package es.in2.dome.blockchainconnector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchainconnector.core.domain.dto.DomeEventDTO;
import es.in2.dome.blockchainconnector.core.exception.JsonReadingException;
import es.in2.dome.blockchainconnector.core.exception.RequestErrorException;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.BlockchainNodeConfig;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.configuration.BlockchainNodeApiConfig;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.domain.BlockchainNotificationDTO;
import es.in2.dome.blockchainconnector.core.integration.orionld.domain.OrionLdNotificationDTO;
import es.in2.dome.blockchainconnector.core.service.HashLinkService;
import es.in2.dome.blockchainconnector.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DomeEventServiceImpl domeEventService;
    private final ObjectMapper objectMapper;
    private final HashLinkService hashLinkService;
    private final BlockchainNodeApiConfig blockchainNodeApiConfig;
    private final BlockchainNodeConfig blockchainNodeConfig;

    @Override
    public void processOrionLdNotification(String data) {
        log.debug("Processing received Orion-LD notification...");
        // Parse the notification data to a JSON object
        OrionLdNotificationDTO orionLdNotificationDTO = mapOrionLdNotification(data);
        // Create a DomeEventDTO from the notification data
        DomeEventDTO domeEventDTO = domeEventService.createDomeEvent(orionLdNotificationDTO);
        // Publish the DomeEventDTO to the blockchain node
        publishDomeEvent(domeEventDTO);
        log.debug("Orion-LD notification processed successfully");
    }

    private void publishDomeEvent(DomeEventDTO domeEventDTO) {
        log.debug("Publishing DOME Event to Blockchain Node...");
        URI uri = URI.create(blockchainNodeApiConfig.getApiDomain() + blockchainNodeApiConfig.getApiPathPublish());
        log.debug(">>> URI: {}", uri);
        String requestBody = toJson(domeEventDTO);
        log.debug(">>> Request Body: {}", requestBody);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response;
        try {
            response = blockchainNodeConfig.blockchainNodeInterfaceHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug(">>> Response: {}", response.statusCode() + ": " + response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
        }
        if (response.statusCode() != 200) {
            throw new RequestErrorException("Error sending request to blockchain node");
        }
        log.debug("DOME Event published to blockchain node successfully");
    }

    @Override
    public void processBlockchainNodeNotification(String data) {
        BlockchainNotificationDTO blockchainNotificationDTO = mapBlockchainNotification(data);
        hashLinkService.resolveHashlink(blockchainNotificationDTO.getDataLocation());
    }

    private BlockchainNotificationDTO mapBlockchainNotification(String data) {
        try {
            return objectMapper.readValue(data, BlockchainNotificationDTO.class);
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error mapping BlockchainNotificationDTO", e);
        }
    }

    private OrionLdNotificationDTO mapOrionLdNotification(String data) {
        try {
            JsonNode jsonNode = objectMapper.readTree(data);
            return new OrionLdNotificationDTO(
                    jsonNode.get("id").toString(),
                    jsonNode.get("type").toString(),
                    jsonNode.get("data").toString(),
                    jsonNode.get("subscriptionId").toString(),
                    jsonNode.get("notifiedAt").toString()
            );
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error mapping OrionLdNotificationDTO", e);
        }
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error parsing object to json", e);
        }
    }

}
