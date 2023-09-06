package es.in2.blockchain.connector.integration.orionld.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.naming.CommunicationException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.APPLICATION_JSON_HEADER;
import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.CONTENT_HEADER;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrionLdIConfig {

    private final OrionLdProperties orionLdProperties;

    @Bean
    @Profile("!default")
    public void setDefaultOrionLdSubscription() {

        log.info(">>> Setting Orion-LD Entities subscription...");

        OrionLdSubscriptionDTO orionLdSubscriptionDTO = OrionLdSubscriptionDTO.builder()
                .id("urn:ngsi-ld:Subscription:" + UUID.randomUUID())
                .type("Subscription")
                .notificationEndpointUri(orionLdProperties.getSubscriptionNotificationEndpointUri())
                .entities(orionLdProperties.getSubscriptionEntities())
                .build();
        log.debug(" > Orion-LD Subscription: {}", orionLdSubscriptionDTO.toString());

        try {
            String orionLdInterfaceUrl = orionLdProperties.getApiDomain() + orionLdProperties.getApiPathSubscription();
            log.debug(" > Orion-LD Subscription URL: {}", orionLdInterfaceUrl);

            String requestBody = new ObjectMapper().writeValueAsString(orionLdSubscriptionDTO);
            log.debug(" > Orion-LD Subscription request body: {}", requestBody);

            // Create request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(orionLdInterfaceUrl))
                    .headers(CONTENT_HEADER, APPLICATION_JSON_HEADER)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Send request asynchronously
            CompletableFuture<HttpResponse<String>> response =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            // Verify Response HttpStatus
            if (response.get().statusCode() != 200) {
                throw new CommunicationException("Error creating default subscription");
            }

            log.info(" > Orion-LD Entities subscription created successfully.");
        } catch (CommunicationException | InterruptedException | ExecutionException | JsonProcessingException e) {
            log.error("Error creating default subscription", e);
            Thread.currentThread().interrupt();
        }
    }

}