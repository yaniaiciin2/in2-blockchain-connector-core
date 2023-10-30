package es.in2.blockchain.connector.integration.blockchainnode.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeDTO;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeSubscriptionDTO;
import es.in2.blockchain.connector.integration.blockchainnode.exception.BlockchainNodeSubscriptionException;
import es.in2.blockchain.connector.integration.orionld.configuration.DLTAdapterProperties;
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

import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.APPLICATION_JSON_HEADER;
import static es.in2.blockchain.connector.core.utils.BlockchainConnectorUtils.CONTENT_HEADER;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BlockchainNodeIConfig {

    private final BlockchainProperties blockchainProperties;
    private final DLTAdapterProperties dltAdapterProperties;
    private final ObjectMapper objectMapper;

    @Bean
    @Profile("!default")
    public CookieManager cookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

    @Bean
    @Profile("!default")
    public HttpClient blockchainNodeInterfaceHttpClient() {
        // Create a single HttpClient instance with a CookieManager to manage cookies
        return HttpClient.newBuilder()
                .cookieHandler(cookieManager())
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
        if(blockchainProperties.subscription().active()) {
            try {
                createDefaultSubscriptionToBlockchainNodeInterface();
            } catch (Exception e) {
                log.error("Error creating default subscription", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void createBlockchainNodeConfiguration() {

        log.info(">>> Creating blockchain node configuration...");

        try {
            String url = dltAdapterProperties.domain() + dltAdapterProperties.paths().configureNode();
            log.debug(" > Blockchain Node Configuration url: {}", url);

            BlockchainNodeDTO blockchainNodeDTO = new BlockchainNodeDTO(blockchainProperties.rpcAddress(),
                    blockchainProperties.userEthereumAddress());
            String body = objectMapper.writer().writeValueAsString(blockchainNodeDTO);
            log.debug(" > Blockchain Node Configuration: {}", body);

            requestCall(URI.create(url), body);
            log.info(" > Blockchain node configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating blockchain node configuration: " + e.getMessage());
        }
    }

    public void createDefaultSubscriptionToBlockchainNodeInterface() {

        log.info(">>> Creating default subscription to blockchain node interface...");

        try {
            String url = dltAdapterProperties.domain() + dltAdapterProperties.paths().subscribe();
            log.debug(" > Blockchain Node I/F Subscription url: {}", url);

            BlockchainNodeSubscriptionDTO blockchainNodeSubscriptionDTO = new BlockchainNodeSubscriptionDTO(
                    blockchainProperties.subscription().eventTypes(),
                    blockchainProperties.subscription().notificationEndpoint());
            String body = objectMapper.writer().writeValueAsString(blockchainNodeSubscriptionDTO);
            log.debug(" > Blockchain Node I/F Subscription body: {}", body);

            requestCall(URI.create(url), body);
            log.info(" > Subscriptions configuration created.");
        } catch (JsonProcessingException e) {
            throw new BlockchainNodeSubscriptionException("Error creating default subscription: " + e.getMessage());
        }
    }

    private void requestCall(URI url, String body) {
        HttpClient httpClient = blockchainNodeInterfaceHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header(CONTENT_HEADER, APPLICATION_JSON_HEADER)
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