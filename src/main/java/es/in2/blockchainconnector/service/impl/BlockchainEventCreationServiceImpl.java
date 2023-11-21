package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
import es.in2.blockchainconnector.configuration.properties.OperatorProperties;
import es.in2.blockchainconnector.domain.OnChainEvent;
import es.in2.blockchainconnector.domain.OnChainEventDTO;
import es.in2.blockchainconnector.exception.HashLinkException;
import es.in2.blockchainconnector.service.BlockchainEventCreationService;
import es.in2.blockchainconnector.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static es.in2.blockchainconnector.utils.Utils.HASHLINK_PREFIX;
import static es.in2.blockchainconnector.utils.Utils.getRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainEventCreationServiceImpl implements BlockchainEventCreationService {

    private final OperatorProperties operatorProperties;
    private final BrokerProperties brokerProperties;

    @Override
    public Mono<OnChainEvent> createBlockchainEvent(OnChainEventDTO onChainEventDTO) {
        String processId = MDC.get("processId");
        return Mono.fromCallable(() -> {
            try {
                log.debug("ProcessID: {} - Creating blockchain event...", processId);
                String entityHashed;
                if (onChainEventDTO.dataMap().containsKey("deletedAt")) {
                    // Calculate SHA-256 hash from origin data
                    entityHashed = Utils.calculateSHA256Hash(getRequest
                            (brokerProperties.internalDomain() + brokerProperties.paths().entities() + "/" + onChainEventDTO.id())
                            .thenApply(HttpResponse::body).join());
                } else {
                    // Calculate SHA-256 hash from the data
                    entityHashed = Utils.calculateSHA256Hash(onChainEventDTO.data());
                }
                // Build dynamic URL by Broker Entity Use Case
                String brokerEntityUrl = brokerProperties.internalDomain() + brokerProperties.paths().entities();
                // Create DataLocation parameter (Hashlink)
                String dataLocation = brokerEntityUrl + "/" + onChainEventDTO.id() + HASHLINK_PREFIX + entityHashed;
                // Build OnChainEvent
                OnChainEvent onChainEvent = OnChainEvent.builder()
                        .eventType(onChainEventDTO.eventType())
                        .organizationId(Utils.calculateSHA256Hash(operatorProperties.organizationId()))
                        .dataLocation(dataLocation)
                        .metadata(List.of())
                        .build();
                log.debug("ProcessID: {} - OnChainEvent created: {}", processId, onChainEvent);
                return onChainEvent;
            } catch (NoSuchAlgorithmException e) {
                log.error("ProcessID: {} - Error creating blockchain event: {}", processId, e.getMessage());
                throw new HashLinkException("Error creating blockchain event", e.getCause());
            }
        }).onErrorMap(NoSuchAlgorithmException.class, e -> new HashLinkException("Error creating blockchain event", e.getCause()));
    }

}
