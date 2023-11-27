package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.domain.TransactionStatus;
import es.in2.blockchainconnector.domain.TransactionTrader;
import es.in2.blockchainconnector.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTransaction_Success() {
        // Arrange
        String processId = "testProcessId";
        MDC.put("processId", processId);

        Transaction transaction = createSampleTransaction();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(transaction));

        // Act
        Mono<Transaction> resultMono = service.saveTransaction(transaction);

        // Assert
        Transaction resultTransaction = resultMono.block();
        assertEquals(transaction, resultTransaction);

    }


    private Transaction createSampleTransaction() {
        return Transaction.builder()
                .id(UUID.randomUUID())
                .transactionId("sampleTransactionId")
                .createdAt(Timestamp.from(Instant.now()))
                .dataLocation("sampleDataLocation")
                .entityId("sampleEntityId")
                .entityHash("sampleEntityHash")
                .status(TransactionStatus.CREATED)
                .trader(TransactionTrader.PRODUCER)
                .hash("sampleHash")
                .newTransaction(true)
                .build();
    }
}

