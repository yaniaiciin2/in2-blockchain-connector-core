package es.in2.dome.blockchain.connector.integration.contextbroker.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.ContextBrokerSubscription;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.SubscriptionEntity;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.SubscriptionNotification;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.SubscriptionNotificationEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ContextBrokerConfig {

    private final ContextBrokerApiConfig contextBrokerProperties;
    private final ContextBrokerSubscriptionConfig contextBrokerSubscriptionProperties;

    @Bean
    @Profile("!default")
    public void setDefaultSubscriptions() throws JsonProcessingException {

        List<SubscriptionEntity> entities = new ArrayList<>();
        contextBrokerSubscriptionProperties.getEntities().forEach(item -> {
            SubscriptionEntity entity = new SubscriptionEntity();
            entity.setType(item.get("type").toString());
            entities.add(entity);
        });
        ContextBrokerSubscription contextBrokerSubscription = new ContextBrokerSubscription();
        contextBrokerSubscription.setEntities(entities);
        contextBrokerSubscription.setId(contextBrokerSubscriptionProperties.getIdPrefix() +
                contextBrokerSubscriptionProperties.getName() + new Date().getTime());
        contextBrokerSubscription.setType(contextBrokerSubscriptionProperties.getType());
        contextBrokerSubscription.setNotification(new SubscriptionNotification(
                new SubscriptionNotificationEndpoint(URLDecoder.decode(
                        contextBrokerSubscriptionProperties.getNotificationEndpointUri(), StandardCharsets.UTF_8))));
        String requestBody = new ObjectMapper().writeValueAsString(contextBrokerSubscription);

        log.debug("Posting subscription to Context Broker: {}", requestBody);

        try {
            postSubscription(requestBody);
        } catch (Exception e) {
            log.error("Error posting subscription to Context Broker: {}", e.getMessage());
            // Clean up whatever needs to be handled before interrupting
            Thread.currentThread().interrupt();
        }
    }

    private void postSubscription(String body) throws InterruptedException, ExecutionException {
        // Set the HttpClient
        HttpClient client = HttpClient.newBuilder().build();
        // Execute Request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(contextBrokerProperties.getSubscriptionUrl()))
                .headers(CONTENT_TYPE, APPLICATION_JSON.toString())
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                .build();
        // Get Response
        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        log.debug("post response = {}", response.get().body());
    }

}
