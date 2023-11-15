package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.core.domain.Transaction;
import es.in2.blockchainconnector.core.repository.TransactionRepository;
import es.in2.blockchainconnector.core.service.impl.AuditServiceImpl;
import es.in2.blockchainconnector.core.service.impl.HashLinkServiceImpl;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

 class AuditServiceImplTest {



    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private HashLinkServiceImpl hashLinkService;

     @InjectMocks
     private AuditServiceImpl auditService;

     @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this);
     }

    @Test
     void testCreateTransaction() {
        // Data
        OnChainEntityDTO onChainEntityDTO = OnChainEntityDTO.builder()
                .id("123")
                .data("Sample Data")
                .build();
        when(hashLinkService.createHashLink(any(), any())).thenReturn(Mono.just("hashed-data"));
        // Act
        Mono<Transaction> createdTransaction = auditService.createTransaction(onChainEntityDTO);
        Transaction transaction = createdTransaction.block();

        // Assert
        assert transaction != null;
        assertEquals("123", transaction.getEntityId());
        assertEquals("RECEIVED", transaction.getStatus());

        verify(transactionRepository).save(transaction);
    }

    @Test
     void testUpdateTransaction() {
        // Data
        Transaction updatedTransaction = Transaction.builder()
                .id(null)
                .entityId("123")
                .entityHash("hashed-data")
                .dataLocation("data-location")
                .status("RECEIVED")
                .build();

        when(transactionRepository.findById(any())).thenReturn(updatedTransaction);

        // Act
        auditService.updateTransaction(updatedTransaction).block();

        // Assert
        verify(transactionRepository).save(updatedTransaction);
    }

    @Test
     void testUpdateTransactionNotFound() {
        // Data
        Transaction updatedTransaction = Transaction.builder()
                .id(null)
                .entityId("123")
                .entityHash("hashed-data")
                .dataLocation("data-location")
                .status("RECEIVED")
                .build();

        when(transactionRepository.findById(any())).thenReturn(null);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> auditService.updateTransaction(updatedTransaction).block());
    }
}

