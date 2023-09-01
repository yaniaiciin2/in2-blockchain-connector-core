package es.in2.dome.blockchain.connector.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.configuration.ContextBrokerConfigApi;
import es.in2.dome.blockchain.connector.domain.entity.DomeEventEntity;
import es.in2.dome.blockchain.connector.exception.HashLinkException;
import es.in2.dome.blockchain.connector.service.DomeEventService;
import es.in2.dome.blockchain.connector.utils.ApplicationUtils;
import es.in2.dome.blockchain.connector.utils.BlockchainConnectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomeEventServiceImpl implements DomeEventService {

    private final ApplicationUtils applicationUtils;
    private final ContextBrokerConfigApi contextBrokerProperties;

    @Override
    public String createDomeEvent(String type, String notificationdata, String id) throws JsonProcessingException, HashLinkException {
        String timestamp = generateTimestamp();
        String dataLocation = createHashLink(notificationdata, id);
        log.debug("DataLocation: " +dataLocation);

        DomeEventEntity domeEventEntity = DomeEventEntity.builder()
                .type(type + BlockchainConnectorUtils.EVENT_PREFIX)
                .dataLocation(dataLocation)
                .timestamp(timestamp)
                .metadata(new ArrayList<>())
                .build();

        // Create ObjectMapper to convert to JSON
        String blockchainEventJson = new ObjectMapper().writeValueAsString(domeEventEntity);
        log.debug(blockchainEventJson);

        log.debug("DOME Event JSON: " + blockchainEventJson);

        return blockchainEventJson;

    }



    private String createHashLink(String resourceData, String id) throws HashLinkException {
        try {
            String resourceHash = applicationUtils.calculateSHA256Hash(resourceData);
            return contextBrokerProperties.getEntitiesUrl() +
                    id +
                    BlockchainConnectorUtils.HASHLINK_PARAMETER +
                    resourceHash;
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
        }
    }

    @Override
    public String createHash(String resourceData, String id) throws HashLinkException {
        try {
            return applicationUtils.calculateSHA256Hash(resourceData);
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
        }
    }




    private String generateTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }





}
