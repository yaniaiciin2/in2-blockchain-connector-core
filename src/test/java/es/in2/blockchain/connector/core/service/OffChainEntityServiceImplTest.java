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
    void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesNotEqual() {
      // Arrange
      String dataLocation = "exampleDataLocation";
      String retrievedEntity = "retrievedEntity"; // Retrieved entity data
      String entityId = "exampleEntityId";
      String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";

      when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(retrievedEntity);
      when(applicationUtils.jsonExtractId(retrievedEntity)).thenReturn(entityId);
      when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn("existingEntity");
      when(applicationUtils.getRequestCode(anyString())).thenReturn("200");
      when(hashLinkService.compareHashLinks(dataLocation, "existingEntity")).thenReturn(false);

      // Act
      offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));

      // Assert
      verify(applicationUtils, times(1)).patchRequest(any(), any());
   }

   @Test
   void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesEqual() {
      // Arrange
      String dataLocation = "exampleDataLocation";
      String retrievedEntity = "retrievedEntity"; // Retrieved entity data
      String entityId = "exampleEntityId";
      String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";

      when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(retrievedEntity);
      when(applicationUtils.jsonExtractId(retrievedEntity)).thenReturn(entityId);
      when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn(retrievedEntity);
      when(applicationUtils.getRequestCode(anyString())).thenReturn("200");
      when(hashLinkService.compareHashLinks(any(), any())).thenReturn(true);

      // Act
      offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));

      // Assert
      verify(applicationUtils, never()).patchRequest(any(), any());
      verify(applicationUtils, never()).postRequest(any(), any());

   }

   @Test
   void testRetrieveAndPublishEntityToOffChain_EntityDoesNotExist() {
      // Arrange
      String dataLocation = "exampleDataLocation";
      String retrievedEntity = "retrievedEntity"; // Retrieved entity data
      String entityId = "exampleEntityId";

      when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(retrievedEntity);
      when(applicationUtils.jsonExtractId(retrievedEntity)).thenReturn(entityId);
      when(applicationUtils.getRequestCode(any())).thenReturn("404");

      // Act
      offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));

      // Assert
       verify(applicationUtils, times(1)).postRequest(any(), any());
   }

   // Helper method to create a BlockchainNodeNotificationDTO
   private BlockchainNodeNotificationDTO createNotification(String dataLocation) {
      return BlockchainNodeNotificationDTO.builder()
              .dataLocation(dataLocation)
              .build();
   }

}
