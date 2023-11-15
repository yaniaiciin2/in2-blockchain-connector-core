package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.integration.dltadapter.domain.DLTNotificationDTO;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;

public interface BrokerEntityValidationService {
    Mono<String> validateEntityIntegrity(String brokerEntity, DLTNotificationDTO dltNotificationDTO);
}
