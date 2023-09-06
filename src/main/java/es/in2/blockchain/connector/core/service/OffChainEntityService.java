package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;

public interface OffChainEntityService {

    void retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO);

}
