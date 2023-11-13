package es.in2.blockchainconnector.integration.brokeradapter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.utils.BlockchainConnectorUtils;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.BrokerAdapterProperties;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.NgsiLdSubscriptionConfigProperties;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OrionLdSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.naming.CommunicationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BrokerAdapterConfig {

    private final BrokerAdapterProperties brokerAdapterProperties;
    private final NgsiLdSubscriptionConfigProperties subscriptionConfiguration;
    private final ObjectMapper objectMapper;

    @Bean
    @Profile("default")
    public void setBrokerSubscription() {
        log.info(">>> Setting Orion-LD Entities subscription...");
        OrionLdSubscriptionDTO orionLdSubscriptionDTO = OrionLdSubscriptionDTO.builder()
                .id("urn:ngsi-ld:Subscription:" + UUID.randomUUID())
                .type("Subscription")
                .notificationEndpointUri(subscriptionConfiguration.notificationEndpoint())
                .entities(subscriptionConfiguration.entityTypes())
                .build();
        log.debug(" > Orion-LD Subscription: {}", orionLdSubscriptionDTO.toString());
        try {
            String orionLdInterfaceUrl = brokerAdapterProperties.domain() + brokerAdapterProperties.paths().subscribe();
            log.debug(" > Orion-LD Subscription URL: {}", orionLdInterfaceUrl);
            String requestBody = objectMapper.writer().writeValueAsString(orionLdSubscriptionDTO);
            log.debug(" > Orion-LD Subscription request body: {}", requestBody);
            // Create request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(orionLdInterfaceUrl))
                    .headers(BlockchainConnectorUtils.CONTENT_HEADER, BlockchainConnectorUtils.APPLICATION_JSON_HEADER,
                            BlockchainConnectorUtils.ACCEPT_HEADER, BlockchainConnectorUtils.APPLICATION_JSON_HEADER)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            // Send request asynchronously
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            // Verify Response HttpStatus
            if (response.get().statusCode() != 200) {
                throw new CommunicationException("Error creating default subscription");
            }
            log.info(" > Orion-LD Entities subscription created successfully.");
        } catch (CommunicationException | InterruptedException | IOException | ExecutionException e) {
            log.error("Error creating default subscription", e);
            Thread.currentThread().interrupt();
        }
    }

}
