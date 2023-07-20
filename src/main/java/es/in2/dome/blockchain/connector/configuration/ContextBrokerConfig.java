package es.in2.dome.blockchain.connector.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.domain.NotificationEndpointDTO;
import es.in2.dome.blockchain.connector.domain.SubscriptionDTO;
import es.in2.dome.blockchain.connector.domain.SubscriptionEntityDTO;
import es.in2.dome.blockchain.connector.domain.SubscriptionNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.UnsupportedEncodingException;
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
    public void setDefaultSubscriptions() throws JsonProcessingException, JSONException {


        List<SubscriptionEntityDTO> entities = new ArrayList<>();

        contextBrokerSubscriptionProperties.getEntities().forEach(item -> {
            SubscriptionEntityDTO entity = new SubscriptionEntityDTO();
            entity.setType(item.get("type").toString());
            entities.add(entity);
        });

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setEntities(entities);
        subscriptionDTO.setId(contextBrokerSubscriptionProperties.getIdPrefix() + contextBrokerSubscriptionProperties.getName() + new Date().getTime());
        subscriptionDTO.setType(contextBrokerSubscriptionProperties.getType());
        subscriptionDTO.setNotification(new SubscriptionNotificationDTO(new NotificationEndpointDTO(URLDecoder.decode(contextBrokerSubscriptionProperties.getNotificationEndpointUri(), StandardCharsets.UTF_8))));


//        JSONObject jsonObject = new JSONObject();
//        JSONArray entities = new JSONArray();
//        contextBrokerSubscriptionProperties.getEntities().forEach(entity -> {
//            JSONObject entityJson = new JSONObject();
//            try {
//                entityJson.put("type", entity.get("type"));
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            entities.put(entityJson);
//        });
//        jsonObject.put("entities", entities);
//        jsonObject.put("id", contextBrokerSubscriptionProperties.getIdPrefix() + contextBrokerSubscriptionProperties.getName() + new Date().getTime());
//        jsonObject.put("type", contextBrokerSubscriptionProperties.getType());
//        JSONObject notification = new JSONObject();
//        JSONObject endpoint = new JSONObject();
//        endpoint.put("uri", URLDecoder.decode(contextBrokerSubscriptionProperties.getNotificationEndpointUri(), StandardCharsets.UTF_8));
//        notification.put("endpoint", endpoint);
//        jsonObject.put("notification", notification);

        String requestBody = new ObjectMapper().writeValueAsString(subscriptionDTO);
        log.debug("Posting subscription to Context Broker: {}", requestBody );

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
