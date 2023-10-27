package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchain.connector.core.service.impl.HashLinkServiceImpl;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerPathProperties;
import es.in2.blockchain.connector.integration.orionld.configuration.BrokerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HashLinkServiceImplTest {

	private HashLinkServiceImpl hashLinkService;

	@Mock
	private ApplicationUtils applicationUtils;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		hashLinkService = new HashLinkServiceImpl(new BrokerProperties("https://example.com", "http://orion-ld:1026",
				new BrokerPathProperties("/entities", "/subscriptions")), applicationUtils);
	}

	@Test
	void createHashLinkShouldReturnCorrectHashlink() throws NoSuchAlgorithmException {
		// Arrange
		String id = "sampleId";
		String data = "sampleData";
		String expectedHashlink = "https://example.com/entities/sampleId?hl=sampleHash";

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

	@Test
	void testCompareHashlinks() throws Exception {
		// Mock the createHashFromEntity method to return a specific value
		when(applicationUtils.calculateSHA256Hash(anyString())).thenReturn("mockedHash");
		// Call the compareHashlinks method with some input parameters
		String dataLocation = "https://example.com/entity?hl=mockedHash";
		String originOffChaiEntity = "This is the entity data";
		boolean result = hashLinkService.compareHashLinksFromEntities(dataLocation, originOffChaiEntity);
		// Assert that the expected result is returned
		assertTrue(result);
		// Verify that the createHashFromEntity method was called with the expected parameter
		verify(applicationUtils).calculateSHA256Hash(originOffChaiEntity);
	}

	@Test
	void testCompareHashlinksWithDifferentOnes() throws Exception {
		// Call the compareHashlinks method with some input parameters
		String targetOffChainEntity = "Something";
		when(applicationUtils.calculateSHA256Hash(targetOffChainEntity)).thenReturn("mockedHash");
		String originOffChaiEntity = "Another thing";
		when(applicationUtils.calculateSHA256Hash(originOffChaiEntity)).thenReturn("anothermockedHash");
		// Assert that the expected result is returned
		assertFalse(hashLinkService.compareHashLinksFromEntities(targetOffChainEntity, originOffChaiEntity));
		// Verify that the createHashFromEntity method was called with the expected parameter
		verify(applicationUtils).calculateSHA256Hash(originOffChaiEntity);
	}
}
