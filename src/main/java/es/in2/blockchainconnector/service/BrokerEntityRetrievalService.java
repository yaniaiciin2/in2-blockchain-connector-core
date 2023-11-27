package es.in2.blockchainconnector.service;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import reactor.core.publisher.Mono;

public interface BrokerEntityRetrievalService {
    Mono<String> retrieveEntityFromSourceBroker(String processId, DLTNotificationDTO dltNotificationDTO);
}
