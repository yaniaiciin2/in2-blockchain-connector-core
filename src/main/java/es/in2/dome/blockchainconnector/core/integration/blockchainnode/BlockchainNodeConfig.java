package es.in2.dome.blockchainconnector.core.integration.blockchainnode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchainconnector.core.exception.BlockchainNodeSubscriptionException;
import es.in2.dome.blockchainconnector.core.exception.RequestErrorException;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.configuration.BlockchainNodeApiConfig;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.domain.BlockchainNodeDTO;
import es.in2.dome.blockchainconnector.core.integration.blockchainnode.domain.BlockchainNodeSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BlockchainNodeConfig {

    private final BlockchainNodeApiConfig blockchainNodeApiConfig;

    @Bean
    public HttpClient blockchainNodeInterfaceHttpClient() {
        // Create a single HttpClient instance with a CookieManager to manage cookies
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
    }

    @Bean
    @Profile("!default")
    public void setDefaultBlockchainNodeConfiguration() {
        try {
            createBlockchainNodeConfiguration();
        } catch (Exception e) {
            log.error("Error creating default subscription", e);
            Thread.currentThread().interrupt();
        }
    }

    @Bean
    @Profile("!default")
    public void setDefaultSubscriptions() {
        try {
            createDefaultSubscriptionToBlockchainNodeInterface();
        } catch (Exception e) {
            log.error("Error creating default subscription", e);
            Thread.currentThread().interrupt();
        }
    }

    public void createBlockchainNodeConfiguration() {
        log.debug("Creating blockchain node configuration...");
        try {
            String url = blockchainNodeApiConfig.getApiDomain() + blockchainNodeApiConfig.getApiPathConfigureNode();
            log.debug(">>> Blockchain Node Configuration url: {}", url);

            BlockchainNodeDTO blockchainNodeDTO = new BlockchainNodeDTO(blockchainNodeApiConfig.getNodeRpcAddress(),
                    blockchainNodeApiConfig.getNodePublicKeyHex());
            String body = new ObjectMapper().writeValueAsString(blockchainNodeDTO);
            log.debug(">>> Blockchain Node Configuration: {}", body);

            requestCall(URI.create(url), body);
            log.debug("Blockchain node configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating blockchain node configuration: " + e.getMessage());
        }
    }

    public void createDefaultSubscriptionToBlockchainNodeInterface() {
        log.debug("Creating default subscription to blockchain node interface...");
        try {
            String url = blockchainNodeApiConfig.getApiDomain() + blockchainNodeApiConfig.getApiPathSubscribe();
            log.debug(">>> Blockchain Node I/F Subscription url: {}", url);

            BlockchainNodeSubscriptionDTO blockchainNodeSubscriptionDTO = new BlockchainNodeSubscriptionDTO(
                    blockchainNodeApiConfig.getSubscriptionEventType(),
                    blockchainNodeApiConfig.getSubscriptionNotificationEndpointUri());
            String body = new ObjectMapper().writeValueAsString(blockchainNodeSubscriptionDTO);
            log.debug(">>> Blockchain Node I/F Subscription body: {}", body);

            requestCall(URI.create(url), body);
            log.debug("Subscriptions configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating default subscription: " + e.getMessage());
        }
    }

    private void requestCall(URI url, String body) {

        HttpClient httpClient = blockchainNodeInterfaceHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug(">>> Response: {}", response.statusCode() + ": " + response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
        }

        if (response.statusCode() != 200) {
            throw new RequestErrorException("Error sending request to blockchain node");
        }

    }

}