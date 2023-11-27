package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.configuration.properties.BrokerPathProperties;
import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
import es.in2.blockchainconnector.configuration.properties.OperatorProperties;
import es.in2.blockchainconnector.domain.BlockchainNodeNotificationIdDTO;
import es.in2.blockchainconnector.domain.BlockchainNodeNotificationTimestampDTO;
import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DLTAdapterNotificationServiceImplTest {

    @InjectMocks
    private DltAdapterNotificationServiceImpl service;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new DltAdapterNotificationServiceImpl(
                transactionService
        );

    }

    @Test
    void processNotification() {
        // Arrange
        DLTNotificationDTO dltNotification = DLTNotificationDTO.builder()
                .id(new BlockchainNodeNotificationIdDTO("BigNumber", "ExampleHex"))
                .publisherAddress("direccionDelEditor")
                .eventType("tipoDeEvento")
                .timestamp(new BlockchainNodeNotificationTimestampDTO("type", "hex"))
                .dataLocation("ubicacionDeDatos")
                .relevantMetadata(List.of("metadata1", "metadata2"))
                .build();

        // Mock the behavior of saveTransaction in TransactionService
        when(transactionService.saveTransaction(any())).thenReturn(Mono.empty());

        // Act
        service.processNotification(dltNotification).then().block();

        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }
}
