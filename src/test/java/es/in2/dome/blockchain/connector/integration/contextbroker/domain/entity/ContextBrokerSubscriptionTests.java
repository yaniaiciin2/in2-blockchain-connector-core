/*
package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContextBrokerSubscriptionTests {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetterAndSetter() {
        // Arrange
        List<SubscriptionEntity> entities = Arrays.asList(
                new SubscriptionEntity("entity1"),
                new SubscriptionEntity("entity2")
        );
        String id = "subscriptionId";
        String type = "subscriptionType";

        SubscriptionNotification notification = new SubscriptionNotification(
                SubscriptionNotificationEndpoint.builder()
                        .uri("url")
                        .build()
        );

        ContextBrokerSubscription subscription = new ContextBrokerSubscription();
        subscription.setEntities(entities);
        subscription.setId(id);
        subscription.setType(type);
        subscription.setNotification(notification);

        // Assert
        assertEquals(entities, subscription.getEntities());
        assertEquals(id, subscription.getId());
        assertEquals(type, subscription.getType());
        assertEquals(notification, subscription.getNotification());
    }

    @Test
    void testBuilder() {
        // Arrange
        List<SubscriptionEntity> entities = Arrays.asList(
                new SubscriptionEntity("entity1"),
                new SubscriptionEntity("entity2")
        );
        String id = "subscriptionId";
        String type = "subscriptionType";
        SubscriptionNotification notification = new SubscriptionNotification(
                SubscriptionNotificationEndpoint.builder()
                        .uri("url")
                        .build());
        // Act
        ContextBrokerSubscription subscription = ContextBrokerSubscription.builder()
                .entities(entities)
                .id(id)
                .type(type)
                .notification(notification)
                .build();
        // Assert
        assertEquals(entities, subscription.getEntities());
        assertEquals(id, subscription.getId());
        assertEquals(type, subscription.getType());
        assertEquals(notification, subscription.getNotification());
    }

    // If you have custom behavior when using ObjectMapper, you can use Mockito to mock it.
    @Test
    void testJsonSerialization() throws Exception {
        // Arrange
        List<SubscriptionEntity> entities = Arrays.asList(
                new SubscriptionEntity("entity1"),
                new SubscriptionEntity("entity2")
        );
        String id = "subscriptionId";
        String type = "subscriptionType";
        SubscriptionNotification notification = new SubscriptionNotification(
                SubscriptionNotificationEndpoint.builder()
                        .uri("url")
                        .build()
        );
        ContextBrokerSubscription subscription = ContextBrokerSubscription.builder()
                .entities(entities)
                .id(id)
                .type(type)
                .notification(notification)
                .build();
        // Mock ObjectMapper
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.writeValueAsString(subscription)).thenReturn("serializedSubscription");
        // Act
        String result = objectMapperMock.writeValueAsString(subscription);
        // Assert
        assertEquals("serializedSubscription", result);
    }

}
*/