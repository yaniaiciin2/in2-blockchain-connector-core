package es.in2.dome.blockchain.connector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.exception.HashLinkException;
import es.in2.dome.blockchain.connector.exception.JsonReadingException;
import es.in2.dome.blockchain.connector.service.HashLinkResolutionService;
import es.in2.dome.blockchain.connector.service.NotificationService;
import es.in2.dome.blockchain.connector.utils.BlockchainConnectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DomeEventServiceImpl domeEventService;
    private final ObjectMapper objectMapper;
    private final HashLinkResolutionService hashLinkResolutionService;

    @Override
    public void processNotification(String data) {
        try {
            JsonNode root = objectMapper.readTree(data);
            String type = root.get(BlockchainConnectorUtils.DATA_FIELD).get(0).get("type").asText();
            String id = root.get(BlockchainConnectorUtils.DATA_FIELD).get(0).get(BlockchainConnectorUtils.ID_FIELD).asText();
            JsonNode eventDataNode = root.get(BlockchainConnectorUtils.DATA_FIELD).get(0);

            // Try to find a data class associated with the type
            Class<?> dataClass = findDataClass();
            if (dataClass != null) {
                // Map the remaining data to the corresponding class
                Object eventData = objectMapper.readValue(root.toString(), dataClass);
                log.debug("Mapped event data: " + eventData);
                createDomeEvent(type, id, eventData);
            } else {
                // Convert the remaining data to a JSON string
                String restOfData = objectMapper.writeValueAsString(eventDataNode);
                createDomeEvent(type, id, restOfData);
            }
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error processing notification", e);
        }
    }

    private Class<?> findDataClass() {
        // We will implement this logic further when we have mapped some data classes
        return null;
    }

    private void createDomeEvent(String type, String id, Object eventData) {
        try {
            // Use type, id, and eventData to create the DomeEvent
            domeEventService.createDomeEvent(type, eventData.toString(), id);
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error creating DomeEvent: JSON processing issue", e);
        } catch (HashLinkException e) {
            throw new HashLinkException("Error while creating hashlink", e);
        }
    }






    private String extractDataLocationValue(String body) throws JsonProcessingException {
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode dataLocationNode = rootNode.get("data_location");

            if (dataLocationNode != null) {
                return dataLocationNode.asText();
            } else {
                log.error("Missing 'data_location' field in the JSON");
            }
        } catch (Exception e) {
            log.error("Error extracting data_location value: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void processEvent(String data) {
        try {
            String datalocation = extractDataLocationValue(data);
            hashLinkResolutionService.resolveHashlink(datalocation);
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error processing event notification", e);
        }


    }


}



