package es.in2.blockchain.connector.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.blockchain.connector.core.exception.RequestErrorException;
import es.in2.blockchain.connector.core.service.HashLinkService;
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

    @Override
    public void createAndPublishEntityToOnChain(OrionLdNotificationDTO orionLdNotificationDTO) {
        // Create On-Chain Entity DTO
        DomeEvent domeEvent = createOnChainEntityDTO(orionLdNotificationDTO);
        // Publish On-Chain Entity DTO to Blockchain Node Interface
        publishDomeEvent(domeEvent);
    }

    private DomeEvent createOnChainEntityDTO(OrionLdNotificationDTO orionLdNotificationDTO) {
        log.debug("Creating On-Chain Entity DTO...");
        Map<String, Object> data = orionLdNotificationDTO.getData().get(0);
        String type = data.get("type").toString();
        String id = data.get("id").toString();
        String dataLocation = hashLinkService.createHashLink(id, data.toString());
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

}
