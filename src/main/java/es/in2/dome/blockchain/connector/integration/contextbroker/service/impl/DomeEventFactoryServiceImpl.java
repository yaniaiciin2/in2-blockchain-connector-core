package es.in2.dome.blockchain.connector.integration.contextbroker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.BlockchainEventEntity;
import es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity.BlockchainEventEntityMetadata;
import es.in2.dome.blockchain.connector.integration.contextbroker.exception.HashLinkException;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.DomeEventFactoryService;
import es.in2.dome.blockchain.connector.utils.HashlinkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomeEventFactoryServiceImpl implements DomeEventFactoryService {

    private final HashlinkUtils hashlinkUtils;

    @Override
    public String createBlockchainEvent(String type, String notificationdata) throws JsonProcessingException, HashLinkException {
        String timestamp = generateTimestamp();
        String dataHashLink = createHashLink(notificationdata);

        BlockchainEventEntity blockchainEventEntity = BlockchainEventEntity.builder()
                .type(type)
                .dataLocation(dataHashLink)
                .timestamp(timestamp)
                .metadata(new BlockchainEventEntityMetadata())
                .build();

        // Create ObjectMapper to convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String blockchainEventJson = objectMapper.writeValueAsString(blockchainEventEntity);


        log.debug("Blockchain Event JSON: " + blockchainEventJson);


        return blockchainEventJson;

    }



    private String createHashLink(String resourceData) throws HashLinkException {
        try {
            String resourceHash = hashlinkUtils.calculateSHA256Hash(resourceData);
            return "hl:" + resourceHash;
        } catch (NoSuchAlgorithmException e) {
            throw new HashLinkException("Error creating Hashlink");
        }
    }


    private String generateTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }





}
