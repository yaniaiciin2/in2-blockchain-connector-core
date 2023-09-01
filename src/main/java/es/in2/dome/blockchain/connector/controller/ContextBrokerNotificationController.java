package es.in2.dome.blockchain.connector.controller;
import es.in2.dome.blockchain.connector.service.NotificationService;
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



    @PostMapping("/orion-ld")
    @ResponseStatus(HttpStatus.CREATED)
    public void receiveNotification(@RequestBody String notification) {
        log.debug("Notification received: {}", notification);
        notificationService.processNotification(notification);

    }

    @PostMapping("/blockchain")
    @ResponseStatus(HttpStatus.OK)
    public void recieveEvent(@RequestBody String event) {
        log.debug("Event received: {}", event);
        notificationService.processEvent(event);

    }

}
