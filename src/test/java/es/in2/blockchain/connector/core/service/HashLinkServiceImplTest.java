package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchain.connector.core.service.impl.HashLinkServiceImpl;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.orionld.configuration.OrionLdProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class HashLinkServiceImplTest {

    @InjectMocks
    private HashLinkServiceImpl hashLinkService;

    @Mock
    private OrionLdProperties orionLdProperties;

    @Mock
    private ApplicationUtils applicationUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createHashLinkShouldReturnCorrectHashlink() throws NoSuchAlgorithmException {
        // Arrange
        String id = "sampleId";
        String data = "sampleData";
        String expectedHashlink = "https://example.com/entities/sampleId?hl=sampleHash";

        when(orionLdProperties.getOrionLdDomain()).thenReturn("https://example.com");
        when(orionLdProperties.getOrionLdPathEntities()).thenReturn("/entities");
        when(applicationUtils.calculateSHA256Hash(data)).thenReturn("sampleHash");

        // Act
        String hashlink = hashLinkService.createHashLink(id, data);

        // Assert
        assertEquals(expectedHashlink, hashlink);
    }

    @Test
    void resolveHashlinkShouldReturnEntityData() throws NoSuchAlgorithmException {
        // Arrange
        String dataLocation = "https://example.com/data-location?hl=sampleHash";
        String expectedEntityData = "sampleEntityData";

        when(applicationUtils.calculateSHA256Hash(expectedEntityData)).thenReturn("sampleHash");
        when(applicationUtils.getRequest("https://example.com/data-location")).thenReturn(expectedEntityData);

        // Act
        String retrievedEntity = hashLinkService.resolveHashlink(dataLocation);

        // Assert
        assertEquals(expectedEntityData, retrievedEntity);
    }


    @Test
    void resolveHashlinkShouldThrowInvalidHashlinkComparison() throws NoSuchAlgorithmException {
        // Arrange
        String dataLocation = "https://example.com/data-location?hl=sampleHash";
        String expectedEntityData = "sampleEntityData";

        when(applicationUtils.calculateSHA256Hash(expectedEntityData)).thenReturn("sampleHash5");
        when(applicationUtils.getRequest("https://example.com/data-location")).thenReturn(expectedEntityData);


        // Assert
        assertThrows(InvalidHashlinkComparisonException.class, () -> hashLinkService.resolveHashlink(dataLocation));
    }
}
