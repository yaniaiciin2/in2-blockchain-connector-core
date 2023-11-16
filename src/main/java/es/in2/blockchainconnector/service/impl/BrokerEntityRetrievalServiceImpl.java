package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.service.BrokerEntityRetrievalService;
import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static es.in2.blockchainconnector.utils.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEntityRetrievalServiceImpl implements BrokerEntityRetrievalService {


    @Override
    public Mono<String> retrieveEntityFromSourceBroker(DLTNotificationDTO dltNotificationDTO) {
        // Retrieve one the entities from the broker
        return Mono.create(sink -> {
            // Get URL from the DLTNotificationDTO.dataLocation()
            String sourceBrokerEntityURL = Arrays.stream(dltNotificationDTO.dataLocation()
                            .split("\\?hl="))
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
            CompletableFuture<HttpResponse<String>> response =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            // Return the response body as a Mono<String>
            response.whenComplete((httpResponse, throwable) -> {
                if (throwable != null) {
                    sink.error(throwable);
                } else {
                    sink.success(httpResponse.body());
                }
            });
        });
    }

}
