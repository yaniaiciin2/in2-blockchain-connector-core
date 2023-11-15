package es.in2.blockchainconnector.service;

import reactor.core.publisher.Mono;

public interface BrokerEntityPublicationService {
    Mono<Void> publishEntityToBroker(String brokerEntity);
}
