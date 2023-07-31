package es.in2.dome.blockchain.connector.integration.contextbroker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class ContextBrokerNotificationController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void receiveNotification(@RequestBody String notification) {
        log.debug("Notification received: {}", notification);
    }

}
