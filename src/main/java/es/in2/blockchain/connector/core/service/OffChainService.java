package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;

public interface OffChainService {

    void retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO);

}
