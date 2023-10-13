package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Override
    public Transaction createTransaction(String entityId, String entityHash) {
        Transaction transaction = Transaction.builder()
                .entityId(entityId)
                .entityHash(entityHash)
                .status("RECIEVED")
                .build();

        return  transactionRepository.save(transaction);
    }

    @Override
    public Transaction editTransactionAttribute(UUID id, String newAttributeValue) {

        Transaction existingTransaction = transactionRepository.findById(id);

        if (existingTransaction != null) {
            existingTransaction.setStatus(newAttributeValue);
            return transactionRepository.save(existingTransaction);
        }

        throw new NoSuchElementException("Transaction not found.");

    }
}
