package es.in2.blockchain.connector.integration.orionld.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.service.OnChainService;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.exception.OrionLdParserException;
import es.in2.blockchain.connector.integration.orionld.service.OrionLdNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrionLdNotificationServiceImpl implements OrionLdNotificationService {

    private final OnChainService onChainService;

    @Override
    public void processNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        try {
            // Get data
            Map<String, Object> dataMap = orionLdNotificationDTO.getData().get(0);
            log.debug(new ObjectMapper().writeValueAsString(dataMap));
            OnChainEntityDTO onChainEntityDTO = OnChainEntityDTO.builder()
                    .id(dataMap.get("id").toString())
                    .eventType(dataMap.get("type").toString())
                    .data(new ObjectMapper().writeValueAsString(dataMap))
                    .build();
            // Send OnChainEntityDTO to OnChainService to be published into the OnChainSystem
            onChainService.publishEntityToOnChainSystem(onChainEntityDTO);
        } catch (Exception e) {
            throw new OrionLdParserException(e.getMessage());
        }
    }

}
