package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.configuration.properties.BrokerAdapterProperties;
import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.exception.RequestErrorException;
import es.in2.blockchainconnector.service.BrokerEntityPublicationService;
import es.in2.blockchainconnector.service.BrokerEntityRetrievalService;
import es.in2.blockchainconnector.service.BrokerEntityValidationService;
import es.in2.blockchainconnector.service.SourceBrokerDataRetrievalServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static es.in2.blockchainconnector.utils.Utils.extractIdFromJson;
import static es.in2.blockchainconnector.utils.Utils.getResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceBrokerDataRetrievalServiceImplFacade implements SourceBrokerDataRetrievalServiceFacade {

    private final BrokerEntityRetrievalService brokerEntityRetrievalService;
    private final BrokerEntityValidationService brokerEntityValidationService;
    private final BrokerEntityPublicationService brokerEntityPublicationService;
    private final BrokerAdapterProperties brokerAdapterProperties;

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
                    return handleResponse(validatedEntity);
                    // todo delete
                })
                .doOnTerminate(() -> log.info("Entity retrieval, validation, and publication completed"));
    }

    private Mono<Void> handleResponse(String validatedEntity) {
        try {
            // Depending on the status code it will decide if update or publish
            int statusCode = getResponseCode(brokerAdapterProperties.domain() +
                    brokerAdapterProperties.paths().entities() +
                    "/" + extractIdFromJson(validatedEntity));

            if (statusCode == 200) {
                log.info(" > Entity exists");
                return brokerEntityPublicationService.updateEntityToBroker(validatedEntity);
            } else if (statusCode == 404) {
                log.info(" > Entity doesn't exist");
                return brokerEntityPublicationService.publishEntityToBroker(validatedEntity);
            } else {
                log.warn("Unhandled response status code: {}", statusCode);
                return Mono.error(new RequestErrorException("Unhandled response status code: " + statusCode));
            }


        } catch (Exception e) {
            log.error("Error handling response", e);
            return Mono.error(e);
        }
    }

}
