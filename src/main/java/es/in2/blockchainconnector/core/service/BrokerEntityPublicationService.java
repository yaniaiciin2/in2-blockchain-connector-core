package es.in2.blockchainconnector.core.service;

import reactor.core.publisher.Mono;

public interface BrokerEntityPublicationService {
    Mono<Void> publishEntityToBroker(String brokerEntity);
}
