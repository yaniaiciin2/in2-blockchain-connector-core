package es.in2.blockchainconnector.core.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import es.in2.blockchainconnector.core.exception.InvalidHashlinkComparisonException;
import es.in2.blockchainconnector.core.service.impl.HashLinkServiceImpl;
import es.in2.blockchainconnector.core.utils.ApplicationUtils;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.BrokerPathProperties;
import es.in2.blockchainconnector.integration.brokeradapter.configuration.properties.BrokerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
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
		ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
				.build();
		hashLinkService = new HashLinkServiceImpl(new BrokerProperties("https://example.com", "http://orion-ld:1026",
				new BrokerPathProperties("/entities", "/subscriptions")), applicationUtils, objectMapper);
	}

	@Test
	void createHashLinkShouldReturnCorrectHashlink() throws NoSuchAlgorithmException {
		// Arrange
		String id = "sampleId";
		String data = "sampleData";
		String expectedHashlink = "https://example.com/entities/sampleId?hl=sampleHash";

		when(applicationUtils.calculateSHA256Hash(data)).thenReturn("sampleHash");

		// Act
		String hashlink = String.valueOf(hashLinkService.createHashLink(id, data).block());

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
		String retrievedEntity = String.valueOf(hashLinkService.resolveHashlink(dataLocation).block());

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
		assertThrows(InvalidHashlinkComparisonException.class, () -> (hashLinkService.resolveHashlink(dataLocation)).block());
	}

	@Test
	void testCompareHashlinks() throws Exception {
		// Mock the createHashFromEntity method to return a specific value
		when(applicationUtils.calculateSHA256Hash(anyString())).thenReturn("mockedHash");
		// Call the compareHashlinks method with some input parameters
		String dataLocation = "https://example.com/entity?hl=mockedHash";
		String originOffChaiEntity = "This is the entity data";
		boolean result = Boolean.TRUE.equals(hashLinkService.compareHashLinksFromEntities(dataLocation, originOffChaiEntity).block());
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
		assertEquals(Boolean.FALSE, hashLinkService.compareHashLinksFromEntities(targetOffChainEntity, originOffChaiEntity).block());
		// Verify that the createHashFromEntity method was called with the expected parameter
		verify(applicationUtils).calculateSHA256Hash(originOffChaiEntity);
	}
}
