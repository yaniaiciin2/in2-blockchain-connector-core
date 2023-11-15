package es.in2.blockchainconnector.core.service.impl;

import es.in2.blockchainconnector.core.service.BrokerEntityPublicationService;
import es.in2.blockchainconnector.core.service.BrokerEntityRetrievalService;
import es.in2.blockchainconnector.core.service.BrokerEntityValidationService;
import es.in2.blockchainconnector.core.service.SourceBrokerDataRetrievalServiceFacade;
import es.in2.blockchainconnector.integration.dltadapter.domain.DLTNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceBrokerDataRetrievalServiceImplFacade implements SourceBrokerDataRetrievalServiceFacade {

    private final BrokerEntityRetrievalService brokerEntityRetrievalService;
    private final BrokerEntityValidationService brokerEntityValidationService;
    private final BrokerEntityPublicationService brokerEntityPublicationService;

    @Override
    public Mono<Void> retrieveAndPublishABrokerEntityIntContextBroker(DLTNotificationDTO dltNotificationDTO) {
        log.info("Retrieving entity from source broker: {}", dltNotificationDTO.toString());
        return brokerEntityRetrievalService.retrieveEntityFromSourceBroker(dltNotificationDTO)
                .flatMap(entityString -> {
                    log.info("Entity retrieved successfully: {}", entityString);
                    return brokerEntityValidationService.validateEntityIntegrity(entityString, dltNotificationDTO);
                })
                .flatMap(validatedEntity -> {
                    log.info("Entity validated successfully: {}", validatedEntity);
                    return brokerEntityPublicationService.publishEntityToBroker(validatedEntity);
                    // create
                    // update
                    // delete
                })
                .doOnTerminate(() -> log.info("Entity retrieval, validation, and publication completed"));
    }

}
