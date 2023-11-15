package es.in2.blockchainconnector.integration.brokeradapter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.service.OnChainService;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import es.in2.blockchainconnector.integration.brokeradapter.domain.BrokerNotificationDTO;
import es.in2.blockchainconnector.integration.brokeradapter.exception.BrokerNotificationParserException;
import es.in2.blockchainconnector.integration.brokeradapter.service.BrokerAdapterNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerAdapterNotificationServiceImpl implements BrokerAdapterNotificationService {

    private final OnChainService onChainService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> processNotification(BrokerNotificationDTO brokerNotificationDTO) {
        return Mono.just(brokerNotificationDTO)
                .flatMap(notification -> {
                    try {
                        // Get and process notification data
                        Map<String, Object> dataMap = notification.data().get(0);
                        String dataToPersist = objectMapper.writeValueAsString(dataMap);
                        // Build OnChainEntityDTO
                        OnChainEntityDTO onChainEntityDTO = OnChainEntityDTO.builder()
                                .id(dataMap.get("id").toString())
                                .eventType(dataMap.get("type").toString())
                                .dataMap(dataMap)
                                .data(dataToPersist)
                                .build();
                        // Send OnChainEntityDTO to OnChainService to be published into the Blockchain
                        return onChainService.publishEntityToOnChainSystem(onChainEntityDTO);
                    } catch (JsonProcessingException e) {
                        // Error processing JSON
                        return Mono.error(
                                new BrokerNotificationParserException("Error processing JSON: " + e.getMessage(), e.getCause()));
                    }
                })
                .onErrorMap(e -> new BrokerNotificationParserException(e.getMessage()))
                .then();
    }

}
