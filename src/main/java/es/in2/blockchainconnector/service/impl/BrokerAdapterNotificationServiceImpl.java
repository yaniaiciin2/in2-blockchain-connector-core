package es.in2.blockchainconnector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.domain.*;
import es.in2.blockchainconnector.exception.BrokerNotificationParserException;
import es.in2.blockchainconnector.service.BrokerAdapterNotificationService;
import es.in2.blockchainconnector.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerAdapterNotificationServiceImpl implements BrokerAdapterNotificationService {

    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    @Override
    public Mono<OnChainEventDTO> processNotification(BrokerNotificationDTO brokerNotificationDTO) {
        String processId = MDC.get("processId");
        return Mono.fromCallable(() -> {
            try {
                // Validate input
                if (brokerNotificationDTO == null || brokerNotificationDTO.data().isEmpty()) {
                    throw new IllegalArgumentException("Invalid BrokerNotificationDTO");
                }
                // Get and process notification data
                Map<String, Object> dataMap = brokerNotificationDTO.data().get(0);
                validateDataMap(dataMap);
                // Build OnChainEventDTO
                String id = dataMap.get("id").toString();
                String eventType = dataMap.get("type").toString();
                String dataToPersist = objectMapper.writeValueAsString(dataMap);
                OnChainEventDTO onChainEventDTO = OnChainEventDTO.builder()
                        .id(id)
                        .eventType(eventType)
                        .dataMap(dataMap)
                        .data(dataToPersist)
                        .build();
                // Build and save Transaction
                Transaction transaction = Transaction.builder()
                        .id(UUID.randomUUID())
                        .transactionId(processId)
                        .createdAt(Timestamp.from(Instant.now()))
                        .dataLocation("")
                        .entityId(id)
                        .entityHash("")
                        .status(TransactionStatus.RECEIVED)
                        .trader(TransactionTrader.PRODUCER)
                        .hash("")
                        .newTransaction(true)
                        .build();
                return transactionService.saveTransaction(transaction)
                        // Return the OnChainEventDTO after the transaction is saved
                        .thenReturn(onChainEventDTO);
            } catch (JsonProcessingException e) {
                // Log error and rethrow
                log.error("ProcessID: {} - Error processing JSON: {}", processId, e.getMessage());
                throw new BrokerNotificationParserException("Error processing JSON", e.getCause());
            }
        }).flatMap(mono -> mono); // Unwrap the Mono<OnChainEventDTO> from Mono<Mono<OnChainEventDTO>>
    }

    private void validateDataMap(Map<String, Object> dataMap) {
        if (dataMap == null || dataMap.get("id") == null || dataMap.get("type") == null) {
            throw new IllegalArgumentException("Invalid dataMap in BrokerNotificationDTO");
        }
    }

}
