package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.OnChainEvent;
import es.in2.blockchainconnector.domain.OnChainEventDTO;
import reactor.core.publisher.Mono;

public interface BlockchainEventCreationService {
    Mono<OnChainEvent> createBlockchainEvent(OnChainEventDTO onChainEventDTO);
}
