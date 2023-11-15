package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.BrokerNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerAdapterNotificationService {

    Mono<Void> processNotification(BrokerNotificationDTO brokerNotificationDTO);

}
