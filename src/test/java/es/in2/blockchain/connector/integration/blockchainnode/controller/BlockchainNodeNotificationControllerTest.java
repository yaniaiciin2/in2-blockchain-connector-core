package es.in2.blockchain.connector.integration.blockchainnode.controller;

import es.in2.blockchain.connector.core.service.OffChainService;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BlockchainNodeNotificationControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private OffChainService offChainService;

    @InjectMocks
    private BlockchainNodeNotificationController nodeNotificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(nodeNotificationController).build();
    }

    @Test
    void retrieveAndPublishEntityToOffChainWithValidDataThenReturn200() {
        String jsonRequest = "{" +
                "\"id\": {" +
                "\"type\": \"urn:ngsi-ld:Notification:b0f522da-489c-11ee-9f5d-0242ac1c0009\"" +
                "}," +
                "\"publisherAddress\": \"publisherAddress123\"," +
                "\"eventType\": \"Event_Type_Test\"," +
                "\"timestamp\": {" +
                "\"type\": \"timestamp_type\"," +
                "\"hex\": \"timestamp_hex\"" +
                "}," +
                "\"dataLocation\": \"https://example.com/data-location\"," +
                "\"relevantMetadata\": [\"metadata1\", \"metadata2\"]" +
                "}";

        when(offChainService.retrieveAndPublishEntityToOffChain(any(BlockchainNodeNotificationDTO.class)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/notifications/blockchain-node")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void retrieveAndPublishEntityToOffChainWithInvalidDataThenReturn400() {
        String jsonRequest = "";

        webTestClient.post()
                .uri("/notifications/blockchain-node")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
