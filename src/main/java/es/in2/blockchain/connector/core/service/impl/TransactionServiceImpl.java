package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.TransactionService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final HashLinkService hashLinkService;

    @Override
    public Transaction createTransaction(String entityId, String entityHash, String datalocation) {
        Transaction transaction = Transaction.builder()
                .entityId(entityId)
                .entityHash(entityHash)
                .dataLocation(datalocation)
                .status(AuditStatus.RECEIVED.getDescription())
                .build();

        return  transactionRepository.save(transaction);
    }

    @Override
    public Transaction editTransaction(Transaction transaction) {
        Transaction transactionFound = transactionRepository.findById(transaction.getId());
        checkIfTransactionExist(transactionFound);
        return transactionRepository.save(transaction);
    }


    private void checkIfTransactionExist(Transaction transactionFound) {
        if (transactionFound == null) {
            throw new NoSuchElementException("Transaction not found.");
        }
    }
}
