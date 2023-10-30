package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.domain.OnChainEntity;
import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.core.service.AuditService;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.OnChainService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeIConfig;
import es.in2.blockchain.connector.integration.orionld.configuration.DLTAdapterProperties;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnChainServiceImpl implements OnChainService {

	private final ObjectMapper objectMapper;
	private final HashLinkService hashLinkService;
	private final BlockchainNodeIConfig blockchainNodeIConfig;
	private final DLTAdapterProperties dltAdapterProperties;
	private final AuditService auditService;

	@Override
	public void publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO) {
		// Create Transaction
		Transaction transaction = auditService.createTransaction(onChainEntityDTO);

		// Create On Chain Entity
		OnChainEntity onChainEntity = createOnChainEntity(onChainEntityDTO, transaction);

		// Publish On Chain Entity into On Chain System (Blockchain Node)
		publishOnChainEntityToBlockchainNode(onChainEntity, transaction);

	}

	private OnChainEntity createOnChainEntity(OnChainEntityDTO onChainEntityDTO, Transaction transaction) {
		log.debug("Creating On-Chain Entity DTO...");
		// Create OnChainEntity
		OnChainEntity onChainEntity = OnChainEntity.builder()
				.eventType(onChainEntityDTO.getEventType())
				.dataLocation(hashLinkService.createHashLink(onChainEntityDTO.getId(), onChainEntityDTO.getData()))
				// todo: the metadata will be added in the next features
				.metadata(List.of("metadata1", "metadata2"))
				.build();
		log.debug(">>> On-Chain Entity DTO: {}", onChainEntity.toString());
		log.debug("On-Chain Entity DTO created successfully.");
		// Update Transaction
		transaction.setEntityHash(hashLinkService.extractHashLink(onChainEntity.getDataLocation()));
		transaction.setStatus(AuditStatus.CREATED.getDescription());
		auditService.updateTransaction(transaction);
		// Return OnChainEntity
		return onChainEntity;
	}

	private void publishOnChainEntityToBlockchainNode(OnChainEntity onChainEntity, Transaction transaction) {
		log.debug("Publishing On-Chain Entity DTO to Blockchain Node Interface...");
		HttpResponse<String> response;
		try {
			// Create URI
			URI uri = URI.create(dltAdapterProperties.domain() + dltAdapterProperties.paths().publish());
			log.debug(">>> URI: {}", uri);
			// Build Request Body
			String requestBody = objectMapper.writer().writeValueAsString(onChainEntity);
			log.debug(">>> Request Body: {}", requestBody);
			log.debug("CookieManager: {}", blockchainNodeIConfig.cookieManager().getCookieStore().getCookies());
			// Make the request
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(uri)
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.header("Content-Type", "application/json")
					.build();
			// Get response
			response = blockchainNodeIConfig.blockchainNodeInterfaceHttpClient()
					.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			log.debug(">>> Response: {}", response.statusCode() + ": " + response.body());
			// Check response
			checkPublishResponse(response);
			// Update Transaction if it is successfully
			transaction.setStatus(AuditStatus.PUBLISHED.getDescription());
			auditService.updateTransaction(transaction);
		} catch (IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
		}
	}

	private void checkPublishResponse(HttpResponse<String> response) {
		if (response.statusCode() == 200) {
			log.debug("On-Chain Entity DTO published successfully.");
		} else {
			throw new RequestErrorException("Error sending request to blockchain node");
		}
	}

}
