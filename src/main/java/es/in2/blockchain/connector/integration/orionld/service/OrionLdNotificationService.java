package es.in2.blockchain.connector.integration.orionld.service;

import es.in2.blockchain.connector.integration.orionld.domain.OrionLdNotificationDTO;
import reactor.core.publisher.Mono;

public interface OrionLdNotificationService {

    Mono<Void> processNotification(OrionLdNotificationDTO orionLdNotificationDTO);

}
