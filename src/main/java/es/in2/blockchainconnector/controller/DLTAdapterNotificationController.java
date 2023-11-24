package es.in2.blockchainconnector.controller;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.service.EntityRetrievalAndPublicationServiceFacade;
import es.in2.blockchainconnector.service.SourceBrokerDataRetrievalServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/notifications/dlt")
@RequiredArgsConstructor
public class DLTAdapterNotificationController {

    private final EntityRetrievalAndPublicationServiceFacade entityRetrievalAndPublicationServiceFacade;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> dltNotification(@RequestBody DLTNotificationDTO dltNotificationDTO) {
        // Create a unique ID for the process
        String processId = UUID.randomUUID().toString();
        // Add the process ID to the Mapped Diagnostic Context (MDC)
        MDC.put("processId", processId);
        // Async Process Start
        log.debug("DLT Notification received: {}", dltNotificationDTO.toString());
        return entityRetrievalAndPublicationServiceFacade.retrieveAndPublishEntityIntoBroker(dltNotificationDTO);
    }

}
