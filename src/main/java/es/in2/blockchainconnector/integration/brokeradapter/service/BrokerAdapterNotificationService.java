package es.in2.blockchainconnector.integration.brokeradapter.service;

import es.in2.blockchainconnector.integration.brokeradapter.domain.BrokerNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerAdapterNotificationService {

    Mono<Void> processNotification(BrokerNotificationDTO brokerNotificationDTO);

}
