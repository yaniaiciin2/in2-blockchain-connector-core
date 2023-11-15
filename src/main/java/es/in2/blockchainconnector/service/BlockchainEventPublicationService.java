package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.OnChainEvent;
import reactor.core.publisher.Mono;

public interface BlockchainEventPublicationService {
    Mono<Void> publishBlockchainEventIntoBlockchainNode(OnChainEvent onChainEvent);
}
