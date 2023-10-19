package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;

public interface TransactionService {

    Transaction createTransactionFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO);

    Transaction updateTransaction(Transaction transaction);

}
