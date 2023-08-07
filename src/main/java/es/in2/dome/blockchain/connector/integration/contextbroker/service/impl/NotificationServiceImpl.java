package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DomeEventFactoryServiceImpl domeEventFactoryService;


    @Override
    public void processNotification(String data) throws JsonProcessingException, HashLinkException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the data to extract the @type field
        JsonNode root = objectMapper.readTree(data);
        String type = root.get("data").get(0).get("type").asText();
        log.debug("@type: " + type);

        // Remove the @type field from the notification
        ((ObjectNode) root).remove("type");

        // Convert the remaining data to a JSON string
        String restOfData = objectMapper.writeValueAsString(root);
        log.debug("Rest of data: " + restOfData);

        domeEventFactoryService.createBlockchainEvent(type, restOfData);
    }
}
