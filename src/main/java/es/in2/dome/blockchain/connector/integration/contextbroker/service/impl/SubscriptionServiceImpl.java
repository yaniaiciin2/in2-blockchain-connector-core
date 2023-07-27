package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerApiConfig;
import es.in2.dome.blockchain.connector.integration.contextbroker.configuration.ContextBrokerSubscriptionConfig;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.ContextBrokerSubscription;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.SubscriptionEntity;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.SubscriptionNotification;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.SubscriptionNotificationEndpoint;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.dto.EntityDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.dto.SubscriptionDTO;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.SubscriptionNotFoundException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ContextBrokerApiConfig contextBrokerProperties;
    private final ContextBrokerSubscriptionConfig contextBrokerSubscriptionProperties;

    @Profile("!default")
    public void createSubscription() throws JsonProcessingException, ExecutionException, InterruptedException {

        List<SubscriptionDTO> subscriptions = parseSubscriptionList(getContextBrokerSubscriptions());

        if (!subscriptions.isEmpty() && subscriptions.stream()
                .anyMatch(subscription -> subscription.getType().equals(contextBrokerSubscriptionProperties.getType()) &&
                        subscription.getEntities().size() == contextBrokerSubscriptionProperties.getEntities().size() &&
                        compareEntityLists(subscription.getEntities(), contextBrokerSubscriptionProperties.getEntities()))) {

            // Found a matching subscription, no action required
            log.debug("Matching subscription found. No action required.");
            return;
        }

        // If no matching subscription is found, build and post a new one
        buildAndPostSubscription();
    }


    private void buildAndPostSubscription() throws JsonProcessingException {

        log.debug("Building subscription...");

        ContextBrokerSubscription newSubscription = buildSubscription();

        String requestBody = new ObjectMapper().writeValueAsString(newSubscription);

        log.debug("Posting subscription to Context Broker: {}", requestBody);

        try {
            postSubscription(requestBody);
        } catch (Exception e) {
            log.error("Error posting subscription to Context Broker: {}", e.getMessage());
            // Clean up whatever needs to be handled before interrupting
            Thread.currentThread().interrupt();
        }

    }

    private boolean compareEntityLists(List<EntityDTO> list1, List<Map<String, Object>> list2) {
        return list1.size() == list2.size() &&
                IntStream.range(0, list1.size())
                        .allMatch(i -> compareEntities(list1.get(i), list2.get(i)));
    }

    private boolean compareEntities(EntityDTO entity1, Map<String, Object> entity2Map) {
        String type1 = entity1.getType();
        String type2 = (String) entity2Map.get("type");
        return type1.equals(type2);
    }

    private ContextBrokerSubscription buildSubscription() {

        List<SubscriptionEntity> entities = new ArrayList<>();
        contextBrokerSubscriptionProperties.getEntities().forEach(item -> entities.add(SubscriptionEntity.builder()
                .type(item.get("type").toString())
                .build()));

        return ContextBrokerSubscription.builder()
                .entities(entities)
                .id(contextBrokerSubscriptionProperties.getIdPrefix() + contextBrokerSubscriptionProperties.getName() + new Date().getTime())
                .type(contextBrokerSubscriptionProperties.getType())
                .notification(new SubscriptionNotification(new SubscriptionNotificationEndpoint
                        (URLDecoder.decode(contextBrokerSubscriptionProperties.getNotificationEndpointUri(), StandardCharsets.UTF_8))))
                .build();
    }



    private void postSubscription(String body) {
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
    }

    private String getContextBrokerSubscriptions() throws ExecutionException, InterruptedException {

        // GET request to obtain subscription list
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(contextBrokerProperties.getSubscriptionUrl()))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Process Response
        HttpResponse<String> httpResponse;
        httpResponse = response.get();


        if (httpResponse.statusCode() == 200) {

            return httpResponse.body();
        } else {
            log.error("Error obtaining subscription list");
            return "[]";
        }

    }



        private List<SubscriptionDTO> parseSubscriptionList(String responseBody) throws JsonProcessingException {
            List<SubscriptionDTO> subscriptionDTOList = new ArrayList<>();

            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            jsonNode.forEach(it -> {
                SubscriptionDTO subs;
                try {
                    subs = new ObjectMapper().readValue(it.toString(), SubscriptionDTO.class);
                    subscriptionDTOList.add(subs);
                } catch (JsonProcessingException e) {
                    throw new SubscriptionNotFoundException("Error while processing JSON");
                }
            });

            return subscriptionDTOList;
        }




}
