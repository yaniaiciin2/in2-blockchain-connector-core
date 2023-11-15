package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.core.domain.OnChainEvent;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEventDTO;
import reactor.core.publisher.Mono;

public interface BlockchainEventCreationService {
    Mono<OnChainEvent> createBlockchainEvent(OnChainEventDTO onChainEventDTO);
}
