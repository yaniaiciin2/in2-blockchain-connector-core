package es.in2.blockchainconnector.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.configuration.properties.BlockchainProperties;
import es.in2.blockchainconnector.configuration.properties.DLTAdapterProperties;
import es.in2.blockchainconnector.domain.BlockchainNodeDTO;
import es.in2.blockchainconnector.domain.BlockchainNodeSubscriptionDTO;
import es.in2.blockchainconnector.exception.BlockchainNodeSubscriptionException;
import es.in2.blockchainconnector.exception.RequestErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static es.in2.blockchainconnector.utils.Utils.APPLICATION_JSON;
import static es.in2.blockchainconnector.utils.Utils.CONTENT_TYPE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DLTAdapterConfig {

    private final ObjectMapper objectMapper;
    private final BlockchainProperties blockchainProperties;
    private final DLTAdapterProperties dltAdapterProperties;

    @Bean
    public CookieManager cookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

    @Bean
    public HttpClient dltAdapterHttpClient() {
        // Create a single HttpClient instance with a CookieManager to manage cookies
        return HttpClient.newBuilder()
                .cookieHandler(cookieManager())
                .build();
    }

    @Bean
    public void setBlockchainNodeConfiguration() {
        try {
            createBlockchainNodeConfiguration();
        } catch (Exception e) {
            log.error("Error creating default subscription", e);
            Thread.currentThread().interrupt();
        }
    }

    private void createBlockchainNodeConfiguration() {
        log.info(">>> Creating blockchain node configuration...");
        try {
            String url = dltAdapterProperties.domain() + dltAdapterProperties.paths().configureNode();
            log.debug(" > Blockchain Node Configuration url: {}", url);
            BlockchainNodeDTO blockchainNodeDTO = new BlockchainNodeDTO(
                    blockchainProperties.rpcAddress(),
                    blockchainProperties.userEthereumAddress());
            String body = objectMapper.writer().writeValueAsString(blockchainNodeDTO);
            log.debug(" > Blockchain Node Configuration: {}", body);
            requestCall(URI.create(url), body);
            log.info(" > Blockchain node configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating blockchain node configuration: " + e.getMessage());
        }
    }

    @Bean
    public void setEventsSubscription() {
        if (blockchainProperties.subscription().active()) {
            try {
                createEventSubscriptionToDLTAdapter();
            } catch (Exception e) {
                log.error("Error creating default subscription", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void createEventSubscriptionToDLTAdapter() {
        log.info(">>> Creating default subscription to blockchain node interface...");
        try {
            String url = dltAdapterProperties.domain() + dltAdapterProperties.paths().subscribe();
            log.debug(" > Blockchain Node I/F Subscription url: {}", url);
            BlockchainNodeSubscriptionDTO blockchainNodeSubscriptionDTO = BlockchainNodeSubscriptionDTO.builder()
                    .eventTypeList(blockchainProperties.subscription().eventTypes())
                    .notificationEndpoint(blockchainProperties.subscription().notificationEndpoint())
                    .build();
            String body = objectMapper.writer().writeValueAsString(blockchainNodeSubscriptionDTO);
            log.debug(" > Blockchain Node I/F Subscription body: {}", body);
            requestCall(URI.create(url), body);
            log.info(" > Subscriptions configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating default subscription: " + e.getMessage());
        }
    }

    private void requestCall(URI url, String body) {
        HttpClient httpClient = dltAdapterHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug(" > Response: {}", response.statusCode() + ": " + response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
        }
        if (response.statusCode() != 200) {
            throw new RequestErrorException("Error sending request to blockchain node");
        }
    }

}
