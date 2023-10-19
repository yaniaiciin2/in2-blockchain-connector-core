package es.in2.blockchain.connector.integration.orionld.service;

import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;

public interface OrionLdNotificationService {

    void processNotification(OrionLdNotificationDTO orionLdNotificationDTO);

}
