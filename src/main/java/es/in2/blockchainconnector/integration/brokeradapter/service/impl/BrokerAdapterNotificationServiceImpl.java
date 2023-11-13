package es.in2.blockchainconnector.integration.brokeradapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.service.OnChainService;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OrionLdNotificationDTO;
import es.in2.blockchainconnector.integration.brokeradapter.exception.OrionLdParserException;
import es.in2.blockchainconnector.integration.brokeradapter.service.BrokerAdapterNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrokerAdapterNotificationServiceImpl implements BrokerAdapterNotificationService {

    private final OnChainService onChainService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> processNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        return Mono.fromCallable(() -> {
            // Get data
            Map<String, Object> dataMap = orionLdNotificationDTO.getData().get(0);
            String dataToPersist = objectMapper.writeValueAsString(dataMap);
            OnChainEntityDTO onChainEntityDTO = OnChainEntityDTO.builder()
                    .id(dataMap.get("id").toString())
                    .eventType(dataMap.get("type").toString())
                    .data(dataToPersist)
                    .build();
            // Send OnChainEntityDTO to OnChainService to be published into the OnChainSystem
            onChainService.publishEntityToOnChainSystem(onChainEntityDTO).subscribe();
            return null; // Null means process finished
        }).doOnError(e -> {
            throw new OrionLdParserException(e.getMessage());
        }).then();
    }

}
