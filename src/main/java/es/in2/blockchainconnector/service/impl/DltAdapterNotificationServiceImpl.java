package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.domain.TransactionStatus;
import es.in2.blockchainconnector.domain.TransactionTrader;
import es.in2.blockchainconnector.service.DltAdapterNotificationService;
import es.in2.blockchainconnector.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DltAdapterNotificationServiceImpl implements DltAdapterNotificationService {

    private final TransactionService transactionService;

    @Override
    public Mono<Void> processNotification(DLTNotificationDTO dltNotificationDTO) {
        String processId = MDC.get("processId");
        return Mono.fromCallable(() -> {
                    try {
                        // Validate input
                        if (dltNotificationDTO == null || dltNotificationDTO.dataLocation().isEmpty()) {
                            throw new IllegalArgumentException("Invalid DLT Notification");
                        }
                        // Build transaction
                        Transaction transaction = Transaction.builder()
                                .id(UUID.randomUUID())
                                .transactionId(processId)
                                .createdAt(Timestamp.from(Instant.now()))
                                .dataLocation(dltNotificationDTO.dataLocation())
                                .entityId("")
                                .entityHash("")
                                .status(TransactionStatus.RECEIVED)
                                .trader(TransactionTrader.CONSUMER)
                                .newTransaction(true)
                                .build();
                        // Save transaction and ignore the result
                        return transactionService.saveTransaction(transaction).then();
                    } catch (Exception e) {
                        log.error("Error processing DLT Notification: {}", e.getMessage(), e);
                        throw e;
                    }
                }).flatMap(mono -> mono)
                .doOnError(error -> log.error("Error processing DLT Notification: {}", error.getMessage(), error));
    }

}
