package es.in2.dome.blockchain.connector.integration.contextbroker.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionNotificationDTO {
    private String format;
    private NotificationEndpointDTO endpoint;
    private String status;
    private int timesSent;
    private String lastNotification;
    private String lastFailure;
    private String lastSuccess;
    private String lastErrorReason;
}
