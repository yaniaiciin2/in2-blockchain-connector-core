package es.in2.dome.blockchain.connector.integration.contextbroker.controller;
import es.in2.dome.blockchain.connector.integration.contextbroker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class ContextBrokerNotificationController {

    private final NotificationService notificationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void receiveNotification(@RequestBody String notification) {
        log.debug("Notification received: {}", notification);
        notificationService.processNotification(notification);

    }

}
