package es.in2.dome.blockchain.connector.integration.contextbroker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class ContextBrokerNotificationController {

    @PostMapping
    public void receiveNotification(@RequestBody String notification) {
        log.debug("Notification received: {}", notification);
    }

}
