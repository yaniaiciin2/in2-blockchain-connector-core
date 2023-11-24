package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.domain.BrokerNotificationDTO;
import es.in2.blockchainconnector.domain.Transaction;
import es.in2.blockchainconnector.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainCreationAndPublicationServiceImplFacade implements BlockchainCreationAndPublicationServiceFacade {

    private final BrokerAdapterNotificationService brokerAdapterNotificationService;
    private final BlockchainEventCreationService blockchainEventCreationService;
    private final BlockchainEventPublicationService blockchainEventPublicationService;
    private final TransactionService transactionService;

    @Override
    public Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(BrokerNotificationDTO brokerNotificationDTO) {
        String processId = MDC.get("processId");
        return brokerAdapterNotificationService.processNotification(brokerNotificationDTO)
                .doOnSuccess(voidValue -> log.info("ProcessID: {} - Broker Notification processed successfully", processId))
                .flatMap(blockchainEventCreationService::createBlockchainEvent)
                .doOnSuccess(voidValue -> log.info("ProcessID: {} - Blockchain Event created successfully", processId))
                .flatMap(blockchainEventPublicationService::publishBlockchainEventIntoBlockchainNode)
                .doOnSuccess(voidValue -> log.info("ProcessID: {} - Blockchain Event published successfully", processId))
                .doOnError(error -> log.error("Error creating or publishing Blockchain Event: {}", error.getMessage(), error));
    }
}
