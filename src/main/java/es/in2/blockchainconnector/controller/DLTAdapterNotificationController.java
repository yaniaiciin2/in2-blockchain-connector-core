package es.in2.blockchainconnector.controller;

import es.in2.blockchainconnector.domain.DLTNotificationDTO;
import es.in2.blockchainconnector.service.SourceBrokerDataRetrievalServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/notifications/dlt")
@RequiredArgsConstructor
public class DLTAdapterNotificationController {

    private final SourceBrokerDataRetrievalServiceFacade sourceBrokerDataRetrievalServiceFacade;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> dltNotification(@RequestBody DLTNotificationDTO dltNotificationDTO) {
        log.debug("DLT Notification received: {}", dltNotificationDTO.toString());
        return sourceBrokerDataRetrievalServiceFacade
                .retrieveAndPublishABrokerEntityIntContextBroker(dltNotificationDTO);
    }

}
