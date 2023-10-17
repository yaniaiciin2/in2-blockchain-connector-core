package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;


public interface TransactionService {
    Transaction createTransaction(String entityId, String entityHash, String datalocation);
    Transaction editTransaction(Transaction transaction);
}
