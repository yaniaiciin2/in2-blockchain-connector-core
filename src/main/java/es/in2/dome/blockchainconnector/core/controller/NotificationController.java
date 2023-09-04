package es.in2.dome.blockchainconnector.core.controller;

import es.in2.dome.blockchainconnector.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/orion-ld")
    @ResponseStatus(HttpStatus.CREATED)
    public void orionLdNotification(@RequestBody String notification) {
        log.debug("Notification received: {}", notification);
        notificationService.processOrionLdNotification(notification);
    }

    @PostMapping("/blockchain-node")
    @ResponseStatus(HttpStatus.OK)
    public void blockchainNodeNotification(@RequestBody String notification) {
        log.debug("Event received: {}", notification);
        notificationService.processBlockchainNodeNotification(notification);
    }

}
