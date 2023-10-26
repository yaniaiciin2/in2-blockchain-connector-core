package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;

public interface OnChainService {
    void publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO);
}
