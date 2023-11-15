package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.integration.dltadapter.domain.DLTNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerEntityRetrievalService {
    Mono<String> retrieveEntityFromSourceBroker(DLTNotificationDTO dltNotificationDTO);
}
