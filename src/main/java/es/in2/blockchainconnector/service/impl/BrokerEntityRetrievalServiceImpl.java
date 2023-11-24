package es.in2.blockchainconnector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.domain.TransactionStatus;
import es.in2.blockchainconnector.domain.TransactionTrader;
import es.in2.blockchainconnector.exception.JsonReadingException;
import es.in2.blockchainconnector.service.BrokerEntityRetrievalService;
import es.in2.blockchainconnector.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static es.in2.blockchainconnector.utils.Utils.ACCEPT_HEADER;
import static es.in2.blockchainconnector.utils.Utils.APPLICATION_JSON;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEntityRetrievalServiceImpl implements BrokerEntityRetrievalService {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<String> retrieveEntityFromSourceBroker(DLTNotificationDTO dltNotificationDTO, String processId) {
        // Retrieve one of the entities from the broker
        return Mono.defer(() -> {
            // Get URL from the DLTNotificationDTO.dataLocation()
            String sourceBrokerEntityURL = Arrays.stream(dltNotificationDTO.dataLocation().split("\\?hl="))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            // Execute a GET request to the Source Context Broker
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(sourceBrokerEntityURL))
                    .headers(ACCEPT_HEADER, APPLICATION_JSON)
                    .GET()
                    .build();


            // Send request asynchronously
            return Mono.fromFuture(() -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                    .flatMap(response -> {
                        try {
                        JsonNode jsonNode = objectMapper.readTree(response.body());
                        String entityId = jsonNode.get("id").asText();
                        // Create and save transaction after receiving the response
                        Transaction transaction = Transaction.builder()
                                .id(UUID.randomUUID())
                                .transactionId(processId)
                                .createdAt(Timestamp.from(Instant.now()))
                                .dataLocation(dltNotificationDTO.dataLocation())
                                .entityId(entityId)
                                .entityHash("")
                                .status(TransactionStatus.RETRIEVED)
                                .trader(TransactionTrader.CONSUMER)
                                .hash("")
                                .newTransaction(true)
                                .build();

                        return transactionService.saveTransaction(transaction)
                                .thenReturn(response.body());
                        } catch (JsonProcessingException e) {
                            return Mono.error(new JsonReadingException("Error while extracting data from entity"));
                        }
                    });
        });
    }

}


