package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;

public interface AuditService {

    Transaction createTransaction(OnChainEntityDTO onChainEntityDTO);

    void updateTransaction(Transaction transaction);

}
