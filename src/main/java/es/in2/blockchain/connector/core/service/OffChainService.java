package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import reactor.core.publisher.Mono;

public interface OffChainService {

    Mono<Void> retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO);

}
