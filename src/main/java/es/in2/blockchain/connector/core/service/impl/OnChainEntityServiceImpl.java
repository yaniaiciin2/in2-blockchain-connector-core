package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.domain.OnChainEntity;
import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.OnChainEntityService;
import es.in2.blockchain.connector.core.service.TransactionService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeIConfig;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeProperties;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdMapper;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnChainEntityServiceImpl implements OnChainEntityService {

    private final HashLinkService hashLinkService;
    private final BlockchainNodeIConfig blockchainNodeIConfig;
    private final BlockchainNodeProperties blockchainNodeProperties;
    private final TransactionService transactionService;

    @Override
    public void createAndPublishEntityToOnChain(OrionLdNotificationDTO orionLdNotificationDTO) {
        // Create transaction
        Transaction transaction = transactionService.createTransactionFromOrionLdNotification(orionLdNotificationDTO);
        // Create OnChain Entity
        OnChainEntity onChainEntity = createOnChainEntity(orionLdNotificationDTO, transaction);
        // Publish On-Chain Entity DTO (DOME Event) to Blockchain Node Interface
        publishOnChainEntityToBlockchainNode(onChainEntity, transaction);
    }

    private OnChainEntity createOnChainEntity(OrionLdNotificationDTO orionLdNotificationDTO, Transaction transaction) {
        try {
            OnChainEntity onChainEntity = buildOnChainEntityFromOrionLdNotification(orionLdNotificationDTO);
            transaction.setEntityHash(hashLinkService.extractHashLink(onChainEntity.getDataLocation()));
            transaction.setStatus(AuditStatus.CREATED.getDescription());
            transactionService.updateTransaction(transaction);
            return onChainEntity;
        } catch (JsonProcessingException e) {
            throw new RequestErrorException("Error creating On-Chain Entity DTO: " + e.getMessage());
        }
    }

    private void publishOnChainEntityToBlockchainNode(OnChainEntity onChainEntity, Transaction transaction) {
        log.debug("Publishing On-Chain Entity DTO to Blockchain Node Interface...");
        HttpResponse<String> response;
        try {
            // Create URI
            URI uri = URI.create(blockchainNodeProperties.getApiDomain() + blockchainNodeProperties.getApiPathPublish());
            log.debug(">>> URI: {}", uri);
            // Build Request Body
            String requestBody = new ObjectMapper().writeValueAsString(onChainEntity);
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
            transactionService.updateTransaction(transaction);
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

    private OnChainEntity buildOnChainEntityFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO)
            throws JsonProcessingException {
        log.debug("Creating On-Chain Entity DTO...");
        // Get data
        Map<String, Object> dataMap = new OrionLdMapper().getDataMapFromOrionLdNotification(orionLdNotificationDTO);
        // Create OnChainEntity
        OnChainEntity onChainEntity = OnChainEntity.builder()
                .eventType(dataMap.get("type").toString())
                .dataLocation(hashLinkService.createHashlinkFromOrionLdNotification(orionLdNotificationDTO))
                .metadata(List.of("metadata1", "metadata2"))
                .build();
        log.debug(">>> On-Chain Entity DTO: {}", onChainEntity.toString());
        log.debug("On-Chain Entity DTO created successfully.");
        return onChainEntity;
    }

}
