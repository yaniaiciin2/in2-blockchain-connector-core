package es.in2.blockchainconnector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.domain.OnChainEvent;
import es.in2.blockchainconnector.core.service.BlockchainEventPublicationService;
import es.in2.blockchainconnector.integration.dltadapter.configuration.DLTAdapterConfig;
import es.in2.blockchainconnector.integration.dltadapter.configuration.properties.DLTAdapterProperties;
import es.in2.blockchainconnector.integration.dltadapter.exception.DLTAdapterCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainEventPublicationServiceImpl implements BlockchainEventPublicationService {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    private final ObjectMapper objectMapper;
    private final DLTAdapterProperties dltAdapterProperties;
    private final DLTAdapterConfig dltAdapterConfig;

    // Publish a Blockchain Event into the Blockchain Node
    @Override
    public Mono<Void> publishBlockchainEventIntoBlockchainNode(OnChainEvent onChainEvent) {
        return Mono.fromRunnable(() -> {
            try {
                log.debug("Publishing On-Chain Event into Blockchain Node...");
                URI uri = URI.create(dltAdapterProperties.domain() + dltAdapterProperties.paths().publish());
                log.debug("URI: {}", uri);
                String requestBody = objectMapper.writeValueAsString(onChainEvent);
                log.debug("Request Body: {}", requestBody);
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .build();
                CompletableFuture<HttpResponse<String>> responseFuture = dltAdapterConfig.dltAdapterHttpClient()
                        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
                // Handle the response when the CompletableFuture completes
                responseFuture.thenAcceptAsync(response -> {
                    log.info("Response status code: {}", response.statusCode());
                    log.info("Response body: {}", response.body());
                    // Add any additional response handling logic here
                    // todo if response.statusCode() != 200 then throw exception
                }).join(); // Use join() to wait for the CompletableFuture to complete
                log.debug("On-Chain Event published successfully into Blockchain Node");
            } catch (JsonProcessingException e) {
                log.error("Error publishing On-Chain Event into Blockchain Node: {}", e.getMessage(), e);
                throw new DLTAdapterCommunicationException("Failed to publish On-Chain Event into Blockchain Node", e);
            }
        });
    }

}
