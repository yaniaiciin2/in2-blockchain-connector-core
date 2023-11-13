package es.in2.blockchainconnector.integration.brokeradapter.service;

import es.in2.blockchainconnector.integration.brokeradapter.domain.OrionLdNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerAdapterNotificationService {

    Mono<Void> processNotification(OrionLdNotificationDTO orionLdNotificationDTO);

}
