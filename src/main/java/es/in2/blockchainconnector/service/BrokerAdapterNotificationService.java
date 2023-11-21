package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.BrokerNotificationDTO;
import es.in2.blockchainconnector.domain.OnChainEventDTO;
import reactor.core.publisher.Mono;

public interface BrokerAdapterNotificationService {

    Mono<OnChainEventDTO> processNotification(BrokerNotificationDTO brokerNotificationDTO);

}
