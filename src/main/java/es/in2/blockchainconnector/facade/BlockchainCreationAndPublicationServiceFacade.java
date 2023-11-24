package es.in2.blockchainconnector.facade;

import es.in2.blockchainconnector.domain.BrokerNotificationDTO;
import reactor.core.publisher.Mono;

public interface BlockchainCreationAndPublicationServiceFacade {
    Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(BrokerNotificationDTO brokerNotificationDTO);
}
