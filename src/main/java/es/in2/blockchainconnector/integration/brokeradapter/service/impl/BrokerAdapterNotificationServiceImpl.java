package es.in2.blockchainconnector.integration.brokeradapter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.service.BlockchainCreationAndPublicationServiceFacade;
import es.in2.blockchainconnector.integration.brokeradapter.domain.BrokerNotificationDTO;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEventDTO;
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

    private final ObjectMapper objectMapper;
    private final BlockchainCreationAndPublicationServiceFacade blockchainCreationAndPublicationServiceFacade;

    @Override
    public Mono<Void> processNotification(BrokerNotificationDTO brokerNotificationDTO) {
        return Mono.fromCallable(() -> {
                    try {
                        // Get and process notification data
                        Map<String, Object> dataMap = brokerNotificationDTO.data().get(0);
                        String dataToPersist = objectMapper.writeValueAsString(dataMap);
                        // Build OnChainEventDTO
                        return OnChainEventDTO.builder()
                                .id(dataMap.get("id").toString())
                                .eventType(dataMap.get("type").toString())
                                .dataMap(dataMap)
                                .data(dataToPersist)
                                .build();
                    } catch (JsonProcessingException e) {
                        // Log error and rethrow
                        log.error("Error processing JSON: {}", e.getMessage(), e);
                        throw new BrokerNotificationParserException("Error processing JSON", e);
                    }
                })
                .flatMap(blockchainCreationAndPublicationServiceFacade::createAndPublishABlockchainEventIntoBlockchainNode);
    }

}
