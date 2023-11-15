//package es.in2.blockchainconnector.core.service;
//
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import es.in2.blockchainconnector.core.service.impl.OffChainServiceImpl;
//import es.in2.blockchainconnector.core.utils.ApplicationUtils;
//import es.in2.blockchainconnector.integration.dltadapter.domain.BlockchainNodeNotificationDTO;
//import es.in2.blockchainconnector.configuration.properties.BrokerPathProperties;
//import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import reactor.core.publisher.Mono;
//
//import java.util.NoSuchElementException;
//
//import static org.mockito.Mockito.*;
//
//class OffChainServiceImplTest {
//
//	@Mock
//	private HashLinkService hashLinkService;
//
//	@Mock
//	private ApplicationUtils applicationUtils;
//
//	@Mock
//
//	private OffChainServiceImpl offChainEntityService;
//
//	@BeforeEach
//	void setUp() {
//		ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
//				.build();
//		MockitoAnnotations.openMocks(this);
//		offChainEntityService = new OffChainServiceImpl(objectMapper, hashLinkService, applicationUtils,
//				new BrokerProperties("https://example.com", "http://orion-ld:1026",
//						new BrokerPathProperties("/entities", "/subscriptions")));
//
//	}
//
//	@Test
//	void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesNotEqual() {
//		// Arrange
//		String dataLocation = "exampleDataLocation";
//		String retrievedEntity = """
//				"{\\n" +
//				    "  \\"@context\\": \\"https://schema.lab.fiware.org/ld/context\\",\\n" +
//				    "  \\"@type\\": \\"Device\\",\\n" +
//				    "  \\"id\\": \\"urn:ngsi-ld:Device:001\\",\\n" +
//				    "  \\"name\\": {\\n" +
//				    "    \\"type\\": \\"Text\\",\\n" +
//				    "    \\"value\\": \\"Sensor de Temperatura\\"\\n" +
//				    "  },\\n" +
//				    "  \\"temperature\\": {\\n" +
//				    "    \\"type\\": \\"Number\\",\\n" +
//				    "    \\"value\\": 25.5,\\n" +
//				    "    \\"metadata\\": {\\n" +
//				    "      \\"unitCode\\": {\\n" +
//				    "        \\"type\\": \\"Text\\",\\n" +
//				    "        \\"value\\": \\"CEL\\"\\n" +
//				    "      }\\n" +
//				    "    }\\n" +
//				    "  },\\n" +
//				    "  \\"location\\": {\\n" +
//				    "    \\"type\\": \\"GeoProperty\\",\\n" +
//				    "    \\"value\\": {\\n" +
//				    "      \\"type\\": \\"Point\\",\\n" +
//				    "      \\"coordinates\\": [-3.7037902, 40.4167754]\\n" +
//				    "    }\\n" +
//				    "  },\\n" +
//				    "  \\"status\\": {\\n" +
//				    "    \\"type\\": \\"Text\\",\\n" +
//				    "    \\"value\\": \\"Operativo\\"\\n" +
//				    "  }\\n" +
//				    "}";"""; // Retrieved entity data
//		String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";
//		Mono <String> monoRetrieved = Mono.just(retrievedEntity);
//
//		when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(monoRetrieved);
//		when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn("existingEntity");
//		when(hashLinkService.compareHashLinksFromEntities(any(), any())).thenReturn(Mono.just(false));
//
//
//		// Act
//		offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation)).block();
//
//		// Assert
//		verify(applicationUtils, times(1)).patchRequest(any(), any());
//	}
//
//	@Test
//	void testRetrieveAndPublishEntityToOffChain_EntityExists_EntitiesEqual() {
//		// Arrange
//		String dataLocation = "exampleDataLocation";
//		String retrievedEntity = """
//				{
//				  "@context": "https://schema.lab.fiware.org/ld/context",
//				  "@type": "Device",
//				  "id": "urn:ngsi-ld:Device:001",
//				  "name": {
//				    "type": "Text",
//				    "value": "Sensor de Temperatura"
//				  },
//				  "temperature": {
//				    "type": "Number",
//				    "value": 25.5,
//				    "metadata": {
//				      "unitCode": {
//				        "type": "Text",
//				        "value": "CEL"
//				      }
//				    }
//				  },
//				  "location": {
//				    "type": "GeoProperty",
//				    "value": {
//				      "type": "Point",
//				      "coordinates": [-3.7037902, 40.4167754]
//				    }
//				  },
//				  "status": {
//				    "type": "Text",
//				    "value": "Operativo"
//				  }
//				}"""; // Retrieved entity data
//		String orionLdEntitiesUrl = "exampleOrionLdEntitiesUrl";
//
//		when(hashLinkService.resolveHashlink(any())).thenReturn(Mono.just(retrievedEntity));
//		when(applicationUtils.getRequest(orionLdEntitiesUrl)).thenReturn(retrievedEntity);
//		when(hashLinkService.compareHashLinksFromEntities(any(), any())).thenReturn(Mono.just(true));
//
//		// Act
//		offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation));
//
//		// Assert
//		verify(applicationUtils, never()).patchRequest(any(), any());
//		verify(applicationUtils, never()).postRequest(any(), any());
//
//	}
//
//	@Test
//	void testRetrieveAndPublishEntityToOffChain_EntityDoesNotExist() {
//		// Arrange
//		String dataLocation = "exampleDataLocation?hl=e021ea41bdde9dc49e266d1d1b9c71a098ff79b86edcc764edc2dc803ca1f927";
//		String retrievedEntity = """
//				{
//				  "@context": "https://schema.lab.fiware.org/ld/context",
//				  "@type": "Device",
//				  "id": "urn:ngsi-ld:Device:001",
//				  "name": {
//				    "type": "Text",
//				    "value": "Sensor de Temperatura"
//				  },
//				  "temperature": {
//				    "type": "Number",
//				    "value": 25.5,
//				    "metadata": {
//				      "unitCode": {
//				        "type": "Text",
//				        "value": "CEL"
//				      }
//				    }
//				  },
//				  "location": {
//				    "type": "GeoProperty",
//				    "value": {
//				      "type": "Point",
//				      "coordinates": [-3.7037902, 40.4167754]
//				    }
//				  },
//				  "status": {
//				    "type": "Text",
//				    "value": "Operativo"
//				  }
//				}""";
//
//		when(hashLinkService.resolveHashlink(dataLocation)).thenReturn(Mono.just(retrievedEntity));
//		when(applicationUtils.getRequest(any())).thenThrow(NoSuchElementException.class);
//
//		// Act
//		offChainEntityService.retrieveAndPublishEntityToOffChain(createNotification(dataLocation)).block();
//
//		// Assert
//		verify(applicationUtils, times(1)).postRequest(any(), any());
//	}
//
//	// Helper method to create a BlockchainNodeNotificationDTO
//	private BlockchainNodeNotificationDTO createNotification(String dataLocation) {
//		return BlockchainNodeNotificationDTO.builder()
//				.dataLocation(dataLocation)
//				.build();
//	}
//
//}
