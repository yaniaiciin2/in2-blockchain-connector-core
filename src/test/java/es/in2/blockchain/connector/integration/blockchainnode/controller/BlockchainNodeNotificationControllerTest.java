
package es.in2.blockchain.connector.integration.blockchainnode.controller;


import es.in2.blockchain.connector.core.service.impl.OffChainEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class BlockchainNodeNotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OffChainEntityServiceImpl offChainEntityService;

    @InjectMocks
    private BlockchainNodeNotificationController nodeNotificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(nodeNotificationController)
                .build();
    }

    @Test
    void retrieveAndPublishEntityToOffChainWithValidDataThenReturn200() throws Exception {

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications/blockchain-node")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }


    @Test
    void retrieveAndPublishEntityToOffChainWithInvalidDataThenReturn400() throws Exception {

        String jsonRequest = "";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications/blockchain-node")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

    }









}
