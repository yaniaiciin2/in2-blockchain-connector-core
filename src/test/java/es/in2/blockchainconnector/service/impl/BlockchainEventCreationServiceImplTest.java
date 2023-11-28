package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.configuration.DLTAdapterConfig;
import es.in2.blockchainconnector.configuration.properties.BrokerPathProperties;
import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
import es.in2.blockchainconnector.configuration.properties.OperatorProperties;
import es.in2.blockchainconnector.domain.OnChainEvent;
import es.in2.blockchainconnector.domain.OnChainEventDTO;
import es.in2.blockchainconnector.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class BlockchainEventCreationServiceImplTest {
    @Mock
    private TransactionService transactionService;

    @Mock
    private DLTAdapterConfig dltAdapterConfig;

    @InjectMocks
    private BlockchainEventCreationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OperatorProperties operatorProperties = new OperatorProperties("VATES-878958");
        BrokerProperties brokerProperties = new BrokerProperties("http://localhost:1026", "http://localhost:1026", new BrokerPathProperties("/v2", "/entities"));
        service = new BlockchainEventCreationServiceImpl(operatorProperties, brokerProperties, transactionService, dltAdapterConfig);

    }

    @Test
    void createBlockchainEventTransaction() throws NoSuchAlgorithmException {
        // Arrange

        String processId = "testProcessId";

        // Create a sample OnChainEventDTO
        OnChainEventDTO onChainEventDTO = OnChainEventDTO.builder()
                .id("sampleId")
                .eventType("sampleEventType")
                .dataMap(Collections.singletonMap("sampleKey", "sampleValue"))
                .data("sampleData")
                .build();

        // Mock the behavior of saveTransaction in TransactionService
        when(transactionService.saveTransaction(any())).thenReturn(Mono.empty());

        // Act
        Mono<OnChainEvent> resultMono = service.createBlockchainEvent(processId, onChainEventDTO);

        // Assert
        OnChainEvent result = resultMono.block(); // Blocks until the Mono is completed
        assert result != null;

        // Verify that saveTransaction was called exactly once with any Transaction object as an argument
        verify(transactionService, times(1)).saveTransaction(any());
    }
}
