package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityRetrievalAndPublicationServiceImplFacade implements EntityRetrievalAndPublicationServiceFacade {

    private final DltAdapterNotificationService dltAdapterNotificationService;
    private final BrokerEntityRetrievalService brokerEntityRetrievalService;
    private final BrokerEntityValidationService brokerEntityValidationService;
    private final BrokerEntityPublicationService brokerEntityPublicationService;
    @Override
    public Mono<Void> retrieveAndPublishEntityIntoBroker(DLTNotificationDTO dltNotificationDTO) {
        String processId = MDC.get("processId");
        return dltAdapterNotificationService.processNotification(dltNotificationDTO)
                .doOnSuccess(voidValue -> log.info("ProcessID: {} - DLT Notification processed successfully", processId))
                .then(Mono.defer(() -> brokerEntityRetrievalService.retrieveEntityFromSourceBroker(dltNotificationDTO, processId)))
                .flatMap(entityString -> {
                    log.info("ProcessID: {} - Entity retrieved successfully {}", processId, entityString);
                    return brokerEntityValidationService.validateEntityIntegrity(String.valueOf(entityString), dltNotificationDTO);
                })
                .flatMap(validatedEntity -> {
                    log.info("ProcessID: {} - Entity validated successfully {}", processId, validatedEntity);
                    return brokerEntityPublicationService.publishOrDeleteAnEntityIntoContextBroker(dltNotificationDTO, validatedEntity, processId);
                }).doOnSuccess(voidValue -> log.info("ProcessID: {} - Entity retrieval, validation, and publication completed", processId));


    }

}
