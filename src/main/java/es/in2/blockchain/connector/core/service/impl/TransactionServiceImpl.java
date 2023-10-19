package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.exception.JsonReadingException;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.TransactionService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final HashLinkService hashLinkService;

    @Override
    public Transaction createTransactionFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO) {
        try {
            Transaction transaction = Transaction.builder()
                    .entityId(orionLdNotificationDTO.getId())
                    .entityHash(" ")
                    .dataLocation(hashLinkService.createHashlinkFromOrionLdNotification(orionLdNotificationDTO))
                    .status(AuditStatus.RECEIVED.getDescription())
                    .build();
            saveTransaction(transaction);
            return transaction;
        } catch (JsonProcessingException e) {
            throw new JsonReadingException(e.getMessage());
        }
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        Transaction transactionFound = transactionRepository.findById(transaction.getId());
        checkIfTransactionExist(transactionFound);
        saveTransaction(transaction);
        return transaction;
    }

    private void checkIfTransactionExist(Transaction transactionFound) {
        if (transactionFound == null) {
            throw new NoSuchElementException("Transaction not found.");
        }
    }

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
