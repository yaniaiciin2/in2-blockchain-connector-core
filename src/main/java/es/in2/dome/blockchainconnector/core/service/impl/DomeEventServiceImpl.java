package es.in2.dome.blockchainconnector.core.service.impl;

import es.in2.dome.blockchainconnector.core.domain.dto.DomeEventDTO;
import es.in2.dome.blockchainconnector.core.integration.orionld.domain.OrionLdNotificationDTO;
import es.in2.dome.blockchainconnector.core.service.DomeEventService;
import es.in2.dome.blockchainconnector.core.service.HashLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomeEventServiceImpl implements DomeEventService {

    private final HashLinkService hashLinkService;

    @Override
    public DomeEventDTO createDomeEvent(OrionLdNotificationDTO orionLdNotificationDTO) {
        String orionLdNotificationDTOData = orionLdNotificationDTO.getData();
        String dataLocation = hashLinkService.createHashLink(orionLdNotificationDTO.getId(),
                orionLdNotificationDTOData);
        return DomeEventDTO.builder()
                .eventType("ProductOffering")
                .dataLocation(dataLocation)
                .metadata(List.of("metadata1", "metadata2"))
                .build();
    }

    // fixme
//    private String getEntityTypeFromData(String data) {
//        try {
//            log.debug(">>> JsonNode2: {}", new ObjectMapper().readTree(data));
//            log.debug(">>> JsonNode1: {}", new ObjectMapper().readTree(data.replace("[{", "").replace("}]", "")));
//
//
//            log.debug(">>> JsonNode3: {}", new ObjectMapper().readTree(data.replace("[{", "").replace("}]", "")).get("type"));
//            log.debug(">>> JsonNode4: {}", new ObjectMapper().readTree(data).get("type"));
//
//            JsonNode jsonNode = new ObjectMapper().readTree(data);
//            log.debug(">>> Type: {}", jsonNode.get("type").toString());
//            return jsonNode.get("type").toString();
//            return "ProductOffering";
//        } catch (JsonProcessingException e) {
//            log.error("Error processing notification", e);
//            throw new RuntimeException(e);
//        }
//    }

}
