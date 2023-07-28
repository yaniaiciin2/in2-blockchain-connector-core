package es.in2.dome.blockchain.connector.integration.contextbroker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionNotificationDTO {
    private String format;
    private NotificationEndpointDTO endpoint;
    private String status;
}
