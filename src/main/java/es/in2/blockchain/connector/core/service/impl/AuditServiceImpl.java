package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.AuditService;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final TransactionRepository transactionRepository;
    private final HashLinkService hashLinkService;

    @Override
    public Mono<Transaction> createTransaction(OnChainEntityDTO onChainEntityDTO) {
        return Mono.fromCallable(() -> {
            String id = onChainEntityDTO.getId();
            Transaction transaction = Transaction.builder()
                    .entityId(id)
                    .entityHash("")
                    .dataLocation(String.valueOf(hashLinkService.createHashLink(id, onChainEntityDTO.getData())))
                    .status(AuditStatus.RECEIVED.getDescription())
                    .build();
            saveTransaction(transaction);
            return transaction;
        });
    }

    @Override
    public Mono<Void> updateTransaction(Transaction transaction) {
        return findTransactionById(transaction.getId())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Transaction not found.")))
                .flatMap(existingTransaction -> {
                    saveTransaction(transaction);
                    return Mono.empty();
                });
    }




    private Mono<Transaction> findTransactionById(UUID id) {
        return Mono.fromCallable(() -> transactionRepository.findById(id));
    }

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
