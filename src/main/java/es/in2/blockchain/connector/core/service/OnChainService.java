package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;

public interface OnChainService {
    void publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO);
}
