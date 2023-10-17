package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.core.exception.JsonReadingException;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.TransactionService;
import es.in2.blockchain.connector.core.utils.AuditStatus;
import es.in2.blockchain.connector.core.utils.EditOperation;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeProperties;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import es.in2.blockchain.connector.core.domain.DomeEvent;
import es.in2.blockchain.connector.core.service.OnChainEntityService;
import es.in2.blockchain.connector.integration.blockchainnode.configuration.BlockchainNodeIConfig;
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
        // Create On-Chain Entity DTO
        DomeEvent domeEvent;
        Transaction transaction;
        transaction = transactionService.createTransaction(orionLdNotificationDTO.getId(), " ", hashLinkService.createHashLink(orionLdNotificationDTO.getId(),parseNotificationData(orionLdNotificationDTO)));
        try {
            domeEvent = createOnChainEntityDTO(orionLdNotificationDTO);
            transactionService.editTransaction(transaction.getId(), hashLinkService.extractHashLink(domeEvent.getDataLocation()), EditOperation.HASH);
            transactionService.editTransaction(transaction.getId(), AuditStatus.CREATED.getDescription(), EditOperation.STATUS);
        } catch (JsonProcessingException e) {
            throw new RequestErrorException("Error creating On-Chain Entity DTO: " + e.getMessage());
        }
        // Publish On-Chain Entity DTO to Blockchain Node Interface
        publishDomeEvent(domeEvent);
        transactionService.editTransaction(transaction.getId(), AuditStatus.PUBLISHED.getDescription(), EditOperation.STATUS);


    }

    private DomeEvent createOnChainEntityDTO(OrionLdNotificationDTO orionLdNotificationDTO) throws JsonProcessingException {
        log.debug("Creating On-Chain Entity DTO...");
        Map<String, Object> data = orionLdNotificationDTO.getData().get(0);
        String type = data.get("type").toString();
        String id = data.get("id").toString();
        String dataString = new ObjectMapper().writeValueAsString(data);
        String dataLocation = hashLinkService.createHashLink(id, dataString);
        DomeEvent domeEvent = DomeEvent.builder()
                .eventType(type)
                .dataLocation(dataLocation)
                .metadata(List.of("metadata1", "metadata2"))
                .build();
        log.debug(">>> On-Chain Entity DTO: {}", domeEvent.toString());
        log.debug("On-Chain Entity DTO created successfully.");
        return domeEvent;
    }

    private void publishDomeEvent(DomeEvent domeEvent) {
        log.debug("Publishing On-Chain Entity DTO to Blockchain Node Interface...");
        HttpResponse<String> response;
        try {
            URI uri = URI.create(blockchainNodeProperties.getApiDomain() + blockchainNodeProperties.getApiPathPublish());
            log.debug(">>> URI: {}", uri);
            String requestBody = new ObjectMapper().writeValueAsString(domeEvent);
            log.debug(">>> Request Body: {}", requestBody);

            log.debug("CookieManager: {}", blockchainNodeIConfig.cookieManager().getCookieStore().getCookies());

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();
            response = blockchainNodeIConfig.blockchainNodeInterfaceHttpClient()
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.debug(">>> Response: {}", response.statusCode() + ": " + response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequestErrorException("Error sending request to blockchain node: " + e.getMessage());
        }

        if (response.statusCode() == 200) {
            log.debug("On-Chain Entity DTO published successfully.");
        } else {
            throw new RequestErrorException("Error sending request to blockchain node");
        }

    }

    private String parseNotificationData(OrionLdNotificationDTO orionLdNotificationDTO) {
        Map<String, Object> data = orionLdNotificationDTO.getData().get(0);
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new JsonReadingException("Error while processing JSON");
        }
    }
}
