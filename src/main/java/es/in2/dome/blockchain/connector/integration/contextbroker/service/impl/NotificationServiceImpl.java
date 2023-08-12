package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.JsonReadingException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DomeEventServiceImpl domeEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void processNotification(String data) {
        try {
            JsonNode root = objectMapper.readTree(data);
            String type = root.get("data").get(0).get("type").asText();
            String id = root.get("data").get(0).get("id").asText();
            log.debug("@type: " + type);
            log.debug("@id: " + id);

            // Try to find a data class associated with the type
            Class<?> dataClass = findDataClass();
            if (dataClass != null) {
                // Map the remaining data to the corresponding class
                Object eventData = objectMapper.readValue(root.toString(), dataClass);
                log.debug("Mapped event data: " + eventData);
                createDomeEvent(type, id, eventData);
            } else {
                // Convert the remaining data to a JSON string
                String restOfData = objectMapper.writeValueAsString(root);
                log.debug("Rest of data: " + restOfData);
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
}



