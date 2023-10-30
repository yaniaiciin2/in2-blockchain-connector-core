package es.in2.blockchain.connector.integration.orionld.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class OrionLdMapper {

    private final ObjectMapper objectMapper;

    public Map<String, Object> getDataMapFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        return orionLdNotificationDTO.getData().get(0);
    }

    public String getDataStringFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO)
            throws JsonProcessingException {
        return objectMapper.writer().writeValueAsString(getDataMapFromOrionLdNotification(orionLdNotificationDTO));
    }

}
