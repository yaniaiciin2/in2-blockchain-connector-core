package es.in2.blockchainconnector.facade;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import reactor.core.publisher.Mono;

public interface EntityRetrievalAndPublicationServiceFacade {
    Mono<Void> retrieveAndPublishEntityIntoBroker(DLTNotificationDTO dltNotificationDTO);
}
