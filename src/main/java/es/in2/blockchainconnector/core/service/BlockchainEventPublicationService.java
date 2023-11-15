package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.core.domain.OnChainEvent;
import reactor.core.publisher.Mono;

public interface BlockchainEventPublicationService {
    Mono<Void> publishBlockchainEventIntoBlockchainNode(OnChainEvent onChainEvent);
}
