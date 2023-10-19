package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.AuditService;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final TransactionRepository transactionRepository;
    private final HashLinkService hashLinkService;

    @Override
    public Transaction createTransaction(OnChainEntityDTO onChainEntityDTO) {
        String id = onChainEntityDTO.getId();
        Transaction transaction = Transaction.builder()
                .entityId(id)
                .entityHash("")
                .dataLocation(hashLinkService.createHashLink(id, onChainEntityDTO.getData()))
                .status(AuditStatus.RECEIVED.getDescription())
                .build();
        saveTransaction(transaction);
        return transaction;
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        Transaction transactionFound = transactionRepository.findById(transaction.getId());
        checkIfTransactionExist(transactionFound);
        saveTransaction(transaction);
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
