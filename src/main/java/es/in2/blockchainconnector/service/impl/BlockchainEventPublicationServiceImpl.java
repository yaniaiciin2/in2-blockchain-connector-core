package es.in2.blockchainconnector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.configuration.DLTAdapterConfig;
import es.in2.blockchainconnector.configuration.properties.DLTAdapterProperties;
import es.in2.blockchainconnector.domain.OnChainEvent;
import es.in2.blockchainconnector.exception.DLTAdapterCommunicationException;
import es.in2.blockchainconnector.service.BlockchainEventPublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static es.in2.blockchainconnector.utils.Utils.APPLICATION_JSON;
import static es.in2.blockchainconnector.utils.Utils.CONTENT_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainEventPublicationServiceImpl implements BlockchainEventPublicationService {

    private final ObjectMapper objectMapper;
    private final DLTAdapterProperties dltAdapterProperties;
    private final DLTAdapterConfig dltAdapterConfig;

    @Override
    public Mono<Void> publishBlockchainEventIntoBlockchainNode(OnChainEvent onChainEvent) {
        String processId = MDC.get("processId");
        return Mono.fromRunnable(() -> {
            try {
                log.debug("ProcessID: {} - Publishing On-Chain Event into Blockchain Node...", processId);
                URI uri = URI.create(dltAdapterProperties.domain() + dltAdapterProperties.paths().publish());
                log.debug("ProcessID: {} - URI: {}", processId, uri);
                String requestBody = objectMapper.writeValueAsString(onChainEvent);
                log.debug("ProcessID: {} - Request Body: {}", processId, requestBody);
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .build();
                CompletableFuture<HttpResponse<String>> responseFuture = dltAdapterConfig.dltAdapterHttpClient()
                        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
                // Handle the response when the CompletableFuture completes
                responseFuture.thenAcceptAsync(response -> log.debug("ProcessID: {} - Response body: {}", processId, response.body())).join(); // Use join() to wait for the CompletableFuture to complete
                log.debug("ProcessID: {} - On-Chain Event published successfully into Blockchain Node", processId);
            } catch (JsonProcessingException e) {
                log.error("ProcessID: {} - Error publishing On-Chain Event into Blockchain Node: {}", processId, e.getMessage());
                throw new DLTAdapterCommunicationException("Failed to publish On-Chain Event into Blockchain Node", e);
            }
        });
    }

}
