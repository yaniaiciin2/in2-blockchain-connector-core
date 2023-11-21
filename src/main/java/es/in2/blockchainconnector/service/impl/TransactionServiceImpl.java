package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.repository.TransactionRepository;
import es.in2.blockchainconnector.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public Mono<Transaction> saveTransaction(Transaction transaction) {
        String processId = MDC.get("processId");
        return transactionRepository.save(transaction)
                .doOnError(error -> log.error("ProcessID: {} - Error saving transaction: {}", processId, error.getMessage()));
    }

    public Mono<Transaction> updateTransaction(Transaction transaction) {
        String processId = MDC.get("processId");
        return transactionRepository.save(transaction)
                .doOnError(error -> log.error("ProcessID: {} - Error updating transaction: {}", processId, error.getMessage()));
    }

}
