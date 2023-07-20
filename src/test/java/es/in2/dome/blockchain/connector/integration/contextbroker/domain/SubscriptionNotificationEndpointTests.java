package es.in2.dome.blockchain.connector.integration.contextbroker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SubscriptionNotificationEndpointTests {

    @Test
    void testGetterAndSetter() {
        // Arrange
        String uri = "https://example.com/notification";
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint();
        // Act
        endpoint.setUri(uri);
        // Assert
        assertEquals(uri, endpoint.getUri());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String uri = "https://example.com/notification";
        // Act
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint(uri);
        // Assert
        assertEquals(uri, endpoint.getUri());
    }

    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint();
        // Assert
        assertNull(endpoint.getUri());
    }

    @Test
    void testBuilder() {
        // Arrange
        String uri = "https://example.com/notification";
        // Act
        SubscriptionNotificationEndpoint endpoint = SubscriptionNotificationEndpoint.builder()
                .uri(uri)
                .build();
        // Assert
        assertEquals(uri, endpoint.getUri());
    }

}
