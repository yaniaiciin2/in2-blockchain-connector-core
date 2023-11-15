package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEventDTO;
import reactor.core.publisher.Mono;

public interface BlockchainCreationAndPublicationServiceFacade {
    Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(OnChainEventDTO onChainEventDTO);
}
