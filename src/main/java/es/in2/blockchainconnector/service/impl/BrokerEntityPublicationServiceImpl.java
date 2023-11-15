package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.service.BrokerEntityPublicationService;
import es.in2.blockchainconnector.configuration.properties.BrokerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static es.in2.blockchainconnector.utils.Utils.postRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerEntityPublicationServiceImpl implements BrokerEntityPublicationService {

    private final BrokerProperties brokerProperties;

    @Override
    public Mono<Void> publishEntityToBroker(String brokerEntity) {
        // Publish the entity to the broker
        String orionLdEntitiesUrl = brokerProperties.internalDomain() + brokerProperties.paths().entities();
        log.debug(" > Publishing entity to: {}", orionLdEntitiesUrl);
        log.debug(" > Entity to publish: {}", brokerEntity);
        return Mono.fromRunnable(() -> postRequest(orionLdEntitiesUrl, brokerEntity));
    }

    // todo Update the entity in the broker

    // todo Delete the entity from the broker

}
