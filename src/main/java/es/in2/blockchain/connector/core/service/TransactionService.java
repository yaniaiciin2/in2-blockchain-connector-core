package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;

import java.util.UUID;


public interface TransactionService {
    Transaction createTransaction(String entityId, String entityHash, String datalocation);
    Transaction editTransactionAttribute(UUID id, String newAttributeValue);
}
