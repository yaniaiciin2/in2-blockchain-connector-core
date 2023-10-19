package es.in2.blockchain.connector.integration.orionld.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrionLdMapper {

    public Map<String, Object> getDataMapFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        return orionLdNotificationDTO.getData().get(0);
    }

    public String getDataStringFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO)
            throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getDataMapFromOrionLdNotification(orionLdNotificationDTO));
    }

}
