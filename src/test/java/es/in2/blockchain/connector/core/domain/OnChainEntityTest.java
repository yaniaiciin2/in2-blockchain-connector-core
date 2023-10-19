package es.in2.blockchain.connector.core.domain;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OnChainEntityTest {

    @Test
    void testDomeEventToString() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";
        List<String> metadata = Arrays.asList("meta1", "meta2");

        OnChainEntity onChainEntity = new OnChainEntity(eventType, dataLocation, metadata);

        // Act
        String result = onChainEntity.toString();

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

        OnChainEntity onChainEntity = new OnChainEntity(eventType, dataLocation, metadata);

        // Act
        String result = onChainEntity.toString();

        // Assert
        String expected = "DOME Event {eventType = SampleEvent, dataLocation = /path/to/data, metadata = []}";
        assertEquals(expected, result);
    }

    @Test
    void testDomeEventToStringWithNullMetadata() {
        // Arrange
        String eventType = "SampleEvent";
        String dataLocation = "/path/to/data";

        OnChainEntity onChainEntity = new OnChainEntity(eventType, dataLocation, null);

        // Act
        String result = onChainEntity.toString();

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

        OnChainEntity onChainEntity = new OnChainEntity(eventType, dataLocation, metadata);

        // Act
        String actualEventType = onChainEntity.getEventType();
        String actualDataLocation = onChainEntity.getDataLocation();
        List<String> actualMetadata = onChainEntity.getMetadata();

        // Assert
        assertEquals(eventType, actualEventType);
        assertEquals(dataLocation, actualDataLocation);
        assertEquals(metadata, actualMetadata);
    }
}



