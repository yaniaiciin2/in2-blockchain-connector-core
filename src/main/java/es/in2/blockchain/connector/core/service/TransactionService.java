package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.utils.EditOperation;

import java.util.UUID;


public interface TransactionService {
    Transaction createTransaction(String entityId, String entityHash, String datalocation);
    Transaction editTransaction(UUID id, String newAttributeValue, EditOperation operation);
}
