//package es.in2.blockchainconnector.integration.domain;
//
//import es.in2.blockchainconnector.integration.dltadapter.domain.BlockchainNodeNotificationDTO;
//import es.in2.blockchainconnector.domain.BlockchainNodeNotificationIdDTO;
//import es.in2.blockchainconnector.domain.BlockchainNodeNotificationTimestampDTO;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class BlockchainNodeNotificationDTOTest {
//
//    @Test
//    void testGetterAndSetterMethods() {
//        // Create DTO objects for testing
//        BlockchainNodeNotificationIdDTO idDTO = BlockchainNodeNotificationIdDTO.builder()
//                .type("type")
//                .hex("hex")
//                .build();
//
//        BlockchainNodeNotificationTimestampDTO timestampDTO = BlockchainNodeNotificationTimestampDTO.builder()
//                .type("type")
//                .hex("hex")
//                .build();
//
//        List<String> relevantMetadata = Arrays.asList("metadata1", "metadata2");
//
//        // Create a BlockchainNodeNotificationDTO object
//        BlockchainNodeNotificationDTO notificationDTO = BlockchainNodeNotificationDTO.builder()
//                .id(idDTO)
//                .publisherAddress("publisherAddress")
//                .eventType("eventType")
//                .timestamp(timestampDTO)
//                .dataLocation("dataLocation")
//                .relevantMetadata(relevantMetadata)
//                .build();
//
//        // Test getter methods
//        assertEquals(idDTO, notificationDTO.getId());
//        assertEquals("publisherAddress", notificationDTO.getPublisherAddress());
//        assertEquals("eventType", notificationDTO.getEventType());
//        assertEquals(timestampDTO, notificationDTO.getTimestamp());
//        assertEquals("dataLocation", notificationDTO.getDataLocation());
//        assertEquals(relevantMetadata, notificationDTO.getRelevantMetadata());
//
//        // Test setter methods
//        BlockchainNodeNotificationIdDTO newIdDTO = BlockchainNodeNotificationIdDTO.builder()
//                .type("newType")
//                .hex("newHex")
//                .build();
//
//        BlockchainNodeNotificationTimestampDTO newTimestampDTO = BlockchainNodeNotificationTimestampDTO.builder()
//                .type("newType")
//                .hex("newHex")
//                .build();
//
//        List<String> newMetadata = Arrays.asList("newMetadata1", "newMetadata2");
//
//        notificationDTO.setId(newIdDTO);
//        notificationDTO.setPublisherAddress("newPublisherAddress");
//        notificationDTO.setEventType("newEventType");
//        notificationDTO.setTimestamp(newTimestampDTO);
//        notificationDTO.setDataLocation("newDataLocation");
//        notificationDTO.setRelevantMetadata(newMetadata);
//
//        // Verify that setter methods updated the values correctly
//        assertEquals(newIdDTO, notificationDTO.getId());
//        assertEquals("newPublisherAddress", notificationDTO.getPublisherAddress());
//        assertEquals("newEventType", notificationDTO.getEventType());
//        assertEquals(newTimestampDTO, notificationDTO.getTimestamp());
//        assertEquals("newDataLocation", notificationDTO.getDataLocation());
//        assertEquals(newMetadata, notificationDTO.getRelevantMetadata());
//    }
//}
