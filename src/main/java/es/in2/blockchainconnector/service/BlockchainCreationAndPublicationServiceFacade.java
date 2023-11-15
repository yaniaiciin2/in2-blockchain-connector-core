package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.OnChainEventDTO;
import reactor.core.publisher.Mono;

public interface BlockchainCreationAndPublicationServiceFacade {
    Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(OnChainEventDTO onChainEventDTO);
}
