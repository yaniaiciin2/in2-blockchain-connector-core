package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.service.impl.OffChainServiceImpl;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerPathProperties;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OffChainServiceImplTest {

	@Mock
	private HashLinkService hashLinkService;

	@Mock
	private ApplicationUtils applicationUtils;

	@Mock

	private OffChainServiceImpl offChainEntityService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		offChainEntityService = new OffChainServiceImpl(hashLinkService, applicationUtils,
				new BrokerProperties("https://example.com", "http://orion-ld:1026",
						new BrokerPathProperties("/entities", "/subscriptions")));

	}

	@Test
	void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesNotEqual() {
		// Arrange
		String dataLocation = "exampleDataLocation";
		String retrievedEntity = """
				"{\\n" +
				    "  \\"@context\\": \\"https://schema.lab.fiware.org/ld/context\\",\\n" +
				    "  \\"@type\\": \\"Device\\",\\n" +
				    "  \\"id\\": \\"urn:ngsi-ld:Device:001\\",\\n" +
				    "  \\"name\\": {\\n" +
				    "    \\"type\\": \\"Text\\",\\n" +
				    "    \\"value\\": \\"Sensor de Temperatura\\"\\n" +
				    "  },\\n" +
				    "  \\"temperature\\": {\\n" +
				    "    \\"type\\": \\"Number\\",\\n" +
				    "    \\"value\\": 25.5,\\n" +
				    "    \\"metadata\\": {\\n" +
				    "      \\"unitCode\\": {\\n" +
				    "        \\"type\\": \\"Text\\",\\n" +
				    "        \\"value\\": \\"CEL\\"\\n" +
				    "      }\\n" +
				    "    }\\n" +
				    "  },\\n" +
				    "  \\"location\\": {\\n" +
				    "    \\"type\\": \\"GeoProperty\\",\\n" +
				    "    \\"value\\": {\\n" +
				    "      \\"type\\": \\"Point\\",\\n" +
				    "      \\"coordinates\\": [-3.7037902, 40.4167754]\\n" +
				    "    }\\n" +
				    "  },\\n" +
				    "  \\"status\\": {\\n" +
				    "    \\"type\\": \\"Text\\",\\n" +
				    "    \\"value\\": \\"Operativo\\"\\n" +
				    "  }\\n" +
				    "}";"""; // Retrieved entity data
		String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";

		when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(retrievedEntity);
		when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn("existingEntity");

		// Act
		offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));

		// Assert
		verify(applicationUtils, times(1)).patchRequest(any(), any());
	}

	@Test
	void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesEqual() {
		// Arrange
		String dataLocation = "exampleDataLocation";
		String retrievedEntity = """
				{
				  "@context": "https://schema.lab.fiware.org/ld/context",
				  "@type": "Device",
				  "id": "urn:ngsi-ld:Device:001",
				  "name": {
				    "type": "Text",
				    "value": "Sensor de Temperatura"
				  },
				  "temperature": {
				    "type": "Number",
				    "value": 25.5,
				    "metadata": {
				      "unitCode": {
				        "type": "Text",
				        "value": "CEL"
				      }
				    }
				  },
				  "location": {
				    "type": "GeoProperty",
				    "value": {
				      "type": "Point",
				      "coordinates": [-3.7037902, 40.4167754]
				    }
				  },
				  "status": {
				    "type": "Text",
				    "value": "Operativo"
				  }
				}"""; // Retrieved entity data
		String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";

		when(hashLinkService.resolveHashlink(any())).thenReturn(retrievedEntity);
		when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn(retrievedEntity);
		when(hashLinkService.compareHashLinksFromEntities(any(), any())).thenReturn(true);

		// Act
		offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));

		// Assert
		verify(applicationUtils, never()).patchRequest(any(), any());
		verify(applicationUtils, never()).postRequest(any(), any());

	}

	@Test
	void testRetrieveAndPublishEntityToOffChain_EntityDoesNotExist() {
		// Arrange
		String dataLocation = "exampleDataLocation?hl=e021ea41bdde9dc49e266d1d1b9c71a098ff79b86edcc764edc2dc803ca1f927";
		String retrievedEntity = """
				{
				  "@context": "https://schema.lab.fiware.org/ld/context",
				  "@type": "Device",
				  "id": "urn:ngsi-ld:Device:001",
				  "name": {
				    "type": "Text",
				    "value": "Sensor de Temperatura"
				  },
				  "temperature": {
				    "type": "Number",
				    "value": 25.5,
				    "metadata": {
				      "unitCode": {
				        "type": "Text",
				        "value": "CEL"
				      }
				    }
				  },
				  "location": {
				    "type": "GeoProperty",
				    "value": {
				      "type": "Point",
				      "coordinates": [-3.7037902, 40.4167754]
				    }
				  },
				  "status": {
				    "type": "Text",
				    "value": "Operativo"
				  }
				}""";

		when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(retrievedEntity);
		when(applicationUtils.getRequest(any())).thenThrow(NoSuchElementException.class);

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
