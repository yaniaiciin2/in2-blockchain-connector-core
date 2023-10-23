package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.repository.TransactionRepository;
import es.in2.blockchain.connector.core.service.impl.AuditServiceImpl;
import es.in2.blockchain.connector.core.service.impl.HashLinkServiceImpl;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        OnChainEntityDTO onChainEntityDTO = new OnChainEntityDTO();
        onChainEntityDTO.setId("123");
        onChainEntityDTO.setData("Sample Data");



        when(hashLinkService.createHashLink(any(), any())).thenReturn("hashed-data");

        // Act
        Transaction createdTransaction = auditService.createTransaction(onChainEntityDTO);

        // Assert
        assertEquals("123", createdTransaction.getEntityId());
        assertEquals("hashed-data", createdTransaction.getDataLocation());
        assertEquals("RECEIVED", createdTransaction.getStatus());

        verify(transactionRepository).save(createdTransaction);
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
        auditService.updateTransaction(updatedTransaction);

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
        assertThrows(NoSuchElementException.class, () -> auditService.updateTransaction(updatedTransaction));
    }
}

