package es.in2.blockchainconnector.core.service.impl;

import es.in2.blockchainconnector.core.configuration.OperatorProperties;
import es.in2.blockchainconnector.core.domain.OnChainEvent;
import es.in2.blockchainconnector.core.exception.HashLinkException;
import es.in2.blockchainconnector.core.service.BlockchainEventCreationService;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.BrokerProperties;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static es.in2.blockchainconnector.core.utils.ApplicationUtils.calculateSHA256Hash;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainEventCreationServiceImpl implements BlockchainEventCreationService {

    public static final String HASHLINK_PARAMETER = "?hl=";
    private final OperatorProperties operatorProperties;
    private final BrokerProperties brokerProperties;

    // Create a Blockchain Event (on-chain event)
    @Override
    public Mono<OnChainEvent> createBlockchainEvent(OnChainEventDTO onChainEventDTO) {
        return Mono.fromCallable(() -> {
            try {
                log.debug(" > Creating blockchain event...");
                // Calculate SHA-256 hash
                String entityHashed = calculateSHA256Hash(onChainEventDTO.data());
                // Build dynamic URL by Broker Entity Use Case
                String brokerEntityUrl = brokerProperties.internalDomain() + brokerProperties.paths().entities();
                // Create DataLocation parameter (Hashlink)
                String dataLocation = brokerEntityUrl + "/" + onChainEventDTO.id() + HASHLINK_PARAMETER + entityHashed;
                // Build OnChainEvent
                OnChainEvent onChainEvent = OnChainEvent.builder()
                        .eventType(onChainEventDTO.eventType())
                        .organizationId(operatorProperties.id())
                        .dataLocation(dataLocation)
                        .metadata(List.of())
                        .build();
                log.debug("OnChainEvent created: {}", onChainEvent);
                return onChainEvent;
            } catch (NoSuchAlgorithmException e) {
                log.error("Error creating blockchain event: {}", e.getMessage(), e);
                throw new HashLinkException("Error creating blockchain event", e);
            }
        }).onErrorMap(NoSuchAlgorithmException.class, e -> new HashLinkException("Error creating blockchain event", e));
    }

}
