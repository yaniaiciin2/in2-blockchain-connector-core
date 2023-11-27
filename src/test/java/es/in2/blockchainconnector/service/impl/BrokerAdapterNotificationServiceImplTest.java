package es.in2.blockchainconnector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import es.in2.blockchainconnector.configuration.properties.BrokerPathProperties;
import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
import es.in2.blockchainconnector.configuration.properties.OperatorProperties;
import es.in2.blockchainconnector.domain.*;
import es.in2.blockchainconnector.exception.JsonReadingException;
import es.in2.blockchainconnector.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class BrokerAdapterNotificationServiceImplTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BrokerAdapterNotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

         service = new BrokerAdapterNotificationServiceImpl(
                new ObjectMapper(),
                transactionService
        );

    }

    @Test
    void processNotificationTest() throws JsonProcessingException {
       Map<String, Object> validDataMap = new HashMap<>();
       validDataMap.put("id", "id_value");
       validDataMap.put("type", "type_value");

       BrokerNotificationDTO brokerNotificationDTO = new BrokerNotificationDTO(
               "id_value",
               "type_value",
               Collections.singletonList(validDataMap),
               "subscriptionId_value",
               "notifiedAt_value"
       );



       Transaction savedTransaction = Transaction.builder()
               .id(UUID.randomUUID())
               .transactionId("processId_value")
               .createdAt(null)
               .dataLocation("")
               .entityId("id_value")
               .entityHash("")
               .status(TransactionStatus.RECEIVED)
               .trader(TransactionTrader.PRODUCER)
               .hash("")
               .newTransaction(true)
               .build();

       Mockito.when(transactionService.saveTransaction(Mockito.any(Transaction.class)))
               .thenReturn(Mono.just(savedTransaction));

       OnChainEventDTO result = service.processNotification(brokerNotificationDTO).block();

       Mockito.verify(transactionService, Mockito.times(1)).saveTransaction(Mockito.any(Transaction.class));
    }

}
