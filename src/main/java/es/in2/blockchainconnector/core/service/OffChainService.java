package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.integration.dltadapter.domain.BlockchainNodeNotificationDTO;
import reactor.core.publisher.Mono;

public interface OffChainService {

    Mono<Void> retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO);

}
