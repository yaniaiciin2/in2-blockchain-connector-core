package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.DomeEventEntity;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.DomeEventService;
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

    @Override
    public String createDomeEvent(String type, String notificationdata) throws JsonProcessingException, HashLinkException {
        String timestamp = generateTimestamp();
        String dataHashLink = createHashLink(notificationdata);

        DomeEventEntity domeEventEntity = DomeEventEntity.builder()
                .type(type)
                .dataLocation(dataHashLink)
                .timestamp(timestamp)
                .metadata(new ArrayList<>())
                .build();

        // Create ObjectMapper to convert to JSON
        String blockchainEventJson = new ObjectMapper().writeValueAsString(domeEventEntity);


        log.debug("Blockchain Event JSON: " + blockchainEventJson);


        return blockchainEventJson;

    }



    private String createHashLink(String resourceData) throws HashLinkException {
        try {
            String resourceHash = applicationUtils.calculateSHA256Hash(resourceData);
            return BlockchainConnectorUtils.HASHLINK_PREFIX + resourceHash;
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
        }
    }


    private String generateTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }





}
