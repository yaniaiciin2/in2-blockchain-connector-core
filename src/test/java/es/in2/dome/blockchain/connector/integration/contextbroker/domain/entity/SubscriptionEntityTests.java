/*
package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SubscriptionEntityTests {

    @Test
    void testGetterAndSetter() {
        // Arrange
        String type = "type1";
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        // Act
        subscriptionEntity.setType(type);
        // Assert
        assertEquals(type, subscriptionEntity.getType());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String type = "type1";
        // Act
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity(type);
        // Assert
        assertEquals(type, subscriptionEntity.getType());
    }

    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        // Assert
        assertNull(subscriptionEntity.getType());
    }

    @Test
    void testBuilder() {
        // Arrange
        String type = "type1";
        // Act
        SubscriptionEntity subscriptionEntity = SubscriptionEntity.builder()
                .type(type)
                .build();
        // Assert
        assertEquals(type, subscriptionEntity.getType());
    }

}
*/