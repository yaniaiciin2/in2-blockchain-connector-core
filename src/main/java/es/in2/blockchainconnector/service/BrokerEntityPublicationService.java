package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerEntityPublicationService {
    Mono<Void> publishOrDeleteAnEntityIntoContextBroker(DLTNotificationDTO dltNotificationDTO, String validatedEntity, String processId);
}
