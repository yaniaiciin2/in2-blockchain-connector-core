package es.in2.dome.blockchain.connector.integration.contextbroker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SubscriptionNotificationTests {

    @Test
    void testGetterAndSetter() {
        // Arrange
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint("url");
        SubscriptionNotification subscriptionNotification = new SubscriptionNotification();
        // Act
        subscriptionNotification.setEndpoint(endpoint);
        // Assert
        assertEquals(endpoint, subscriptionNotification.getEndpoint());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint("url");
        // Act
        SubscriptionNotification subscriptionNotification = new SubscriptionNotification(endpoint);
        // Assert
        assertEquals(endpoint, subscriptionNotification.getEndpoint());
    }

    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        SubscriptionNotification subscriptionNotification = new SubscriptionNotification();
        // Assert
        assertNull(subscriptionNotification.getEndpoint());
    }

    @Test
    void testBuilder() {
        // Arrange
        SubscriptionNotificationEndpoint endpoint = new SubscriptionNotificationEndpoint("url");
        // Act
        SubscriptionNotification subscriptionNotification = SubscriptionNotification.builder()
                .endpoint(endpoint)
                .build();
        // Assert
        assertEquals(endpoint, subscriptionNotification.getEndpoint());
    }

}
