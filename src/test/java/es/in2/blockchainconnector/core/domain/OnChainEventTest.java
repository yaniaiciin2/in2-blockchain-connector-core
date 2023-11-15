package es.in2.blockchainconnector.core.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OnChainEventTest {

    @Test
    void testDomeEventToString() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";
        List<String> metadata = Arrays.asList("meta1", "meta2");

        OnChainEvent onChainEvent = OnChainEvent.builder()
                .eventType(eventType)
                .dataLocation(dataLocation)
                .metadata(metadata).build();

        // Act
        String result = onChainEvent.toString();

        // Assert
        String expected = "DOME Event {eventType = SampleEvent, dataLocation = /path/to/data, metadata = [meta1, meta2]}";
        assertEquals(expected, result);
    }

    @Test
    void testDomeEventToStringWithEmptyMetadata() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";
        List<String> metadata = List.of();

        OnChainEvent onChainEvent = OnChainEvent.builder()
                .eventType(eventType)
                .dataLocation(dataLocation)
                .metadata(metadata).build();


        // Act
        String result = onChainEvent.toString();

        // Assert
        String expected = "DOME Event {eventType = SampleEvent, dataLocation = /path/to/data, metadata = []}";
        assertEquals(expected, result);
    }

    @Test
    void testDomeEventToStringWithNullMetadata() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";

        OnChainEvent onChainEvent = OnChainEvent.builder()
                .eventType(eventType)
                .dataLocation(dataLocation)
                .metadata(null).build();


        // Act
        String result = onChainEvent.toString();

        // Assert
        String expected = "DOME Event {eventType = SampleEvent, dataLocation = /path/to/data, metadata = null}";
        assertEquals(expected, result);
    }

    @Test
    void testDomeEventGetters() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";
        List<String> metadata = Arrays.asList("meta1", "meta2");

        OnChainEvent onChainEvent = OnChainEvent.builder()
                .eventType(eventType)
                .dataLocation(dataLocation)
                .metadata(metadata).build();


        // Act
        String actualEventType = onChainEvent.eventType();
        String actualDataLocation = onChainEvent.dataLocation();
        List<String> actualMetadata = onChainEvent.metadata();

        // Assert
        assertEquals(eventType, actualEventType);
        assertEquals(dataLocation, actualDataLocation);
        assertEquals(metadata, actualMetadata);
    }
}



