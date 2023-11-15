package es.in2.blockchainconnector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchainconnector.core.configuration.OperatorProperties;
import es.in2.blockchainconnector.core.domain.OnChainEntity;
import es.in2.blockchainconnector.core.domain.Transaction;
import es.in2.blockchainconnector.core.exception.JsonReadingException;
import es.in2.blockchainconnector.core.exception.RequestErrorException;
import es.in2.blockchainconnector.core.service.AuditService;
import es.in2.blockchainconnector.core.service.HashLinkService;
import es.in2.blockchainconnector.core.service.OnChainService;
import es.in2.blockchainconnector.core.utils.AuditStatus;
import es.in2.blockchainconnector.integration.dltadapter.configuration.DLTAdapterConfig;
import es.in2.blockchainconnector.integration.dltadapter.configuration.properties.DLTAdapterProperties;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
	private final DLTAdapterConfig dltAdapterConfig;
	private final DLTAdapterProperties dltAdapterProperties;
	private final AuditService auditService;
	private final OperatorProperties operatorProperties;

	@Override
	public Mono<Void> publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO) {
		return auditService.createTransaction(onChainEntityDTO)
				.flatMap(transaction -> createOnChainEntity(onChainEntityDTO, transaction)
						.flatMap(onChainEntity -> publishOnChainEntityToBlockchainNode(onChainEntity, transaction)
								.then(auditService.updateTransaction(transaction)
										.thenReturn(Mono.empty())))).then(); // Aquí asumimos que updateTransaction devuelve Mono<Void>
	}



	private Mono<Void> publishOnChainEntityToBlockchainNode(OnChainEntity onChainEntity, Transaction transaction) {
		return Mono.fromRunnable(() -> {
					log.debug("Publishing On-Chain Entity DTO to Blockchain Node Interface...");
					// Create URI
					URI uri = URI.create(dltAdapterProperties.domain() + dltAdapterProperties.paths().publish());
					log.debug(">>> URI: {}", uri);
					// Build Request Body
					String requestBody;
					try {
						requestBody = objectMapper.writer().writeValueAsString(onChainEntity);
					} catch (JsonProcessingException e) {
						throw new JsonReadingException("Failed to create OnChainEntity");
					}
					log.debug(">>> Request Body: {}", requestBody);
					log.debug("CookieManager: {}", dltAdapterConfig.cookieManager().getCookieStore().getCookies());
					// Make the request
					HttpRequest httpRequest = HttpRequest.newBuilder()
							.uri(uri)
							.POST(HttpRequest.BodyPublishers.ofString(requestBody))
							.header("Content-Type", "application/json")
							.build();
					try {
						// Get response
						HttpResponse<String> response = dltAdapterConfig.dltAdapterHttpClient()
								.send(httpRequest, HttpResponse.BodyHandlers.ofString());
						log.debug(">>> Response: {}", response.statusCode() + ": " + response.body());
						// Check response
						checkPublishResponse(response);
						transaction.setStatus(AuditStatus.PUBLISHED.getDescription());
					} catch (IOException | InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				})
				.then(auditService.updateTransaction(transaction))
				.doOnError(e -> {
					Thread.currentThread().interrupt();
					throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
				})
				.then();
	}


	private void checkPublishResponse(HttpResponse<String> response) {
		if (response.statusCode() != 200) {
			throw new RequestErrorException("Error sending request to blockchain node");
		}
	}

	private Mono<OnChainEntity> createOnChainEntity(OnChainEntityDTO onChainEntityDTO, Transaction transaction) {
		return hashLinkService.createHashLink(onChainEntityDTO.id(), onChainEntityDTO.data())
				.flatMap(dataLocation -> {
					log.debug("Creating On-Chain Entity DTO...");
					OnChainEntity onChainEntity = OnChainEntity.builder()
							.eventType(onChainEntityDTO.eventType())
							.organizationId(operatorProperties.id())
							.dataLocation(dataLocation)
							.metadata(List.of("metadata1", "metadata2"))
							.build();
					log.debug(">>> On-Chain Entity DTO: {}", onChainEntity.toString());
					log.debug("On-Chain Entity DTO created successfully");
					transaction.setEntityHash(String.valueOf(hashLinkService.extractHashLink(dataLocation)));
					transaction.setStatus(AuditStatus.CREATED.getDescription());
					return auditService.updateTransaction(transaction).thenReturn(onChainEntity);
				});
	}


}
