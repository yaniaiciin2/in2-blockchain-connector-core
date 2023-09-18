package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.service.impl.OffChainEntityServiceImpl;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.configuration.OrionLdProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class OffChainEntityServiceImplTest {

    @Mock
    private HashLinkService hashLinkService;

    @Mock
    private ApplicationUtils applicationUtils;

    @Mock
    private OrionLdProperties orionLdProperties;

    @InjectMocks
    private OffChainEntityServiceImpl offChainEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offChainEntityService = new OffChainEntityServiceImpl(hashLinkService, applicationUtils, orionLdProperties);

    }

    @Test
    void testRetrieveAndPublishEntityToOffChain() {
        // Arrange
        BlockchainNodeNotificationDTO notificationDTO = new BlockchainNodeNotificationDTO();
        notificationDTO.setDataLocation("sampleDataLocation");
        String retrievedEntity = "sampleEntity";

        when(hashLinkService.resolveHashlink(any())).thenReturn(retrievedEntity);
        when(orionLdProperties.getApiDomain()).thenReturn("https://example.com");
        when(orionLdProperties.getApiPathEntities()).thenReturn("/entities");

        // Act
        offChainEntityService.retrieveAndPublishEntityToOffChain(notificationDTO);

        // Assert
        // You can add assertions based on your specific logic and behavior.
        verify(hashLinkService, times(1)).resolveHashlink("sampleDataLocation");
        verify(applicationUtils, times(1)).postRequest("https://example.com/entities", "sampleEntity");
    }
}
