package es.in2.blockchain.connector.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;

public interface HashLinkService {
    String createHashLink(String id, String data);
    String resolveHashlink(String dataLocation);
    boolean compareHashLinksFromEntities(String dataLocation, String originOffChaiEntity);
    String extractHashLink(String url);

    String createHashlinkFromOrionLdNotification(OrionLdNotificationDTO orionLdNotificationDTO) throws JsonProcessingException;
}
