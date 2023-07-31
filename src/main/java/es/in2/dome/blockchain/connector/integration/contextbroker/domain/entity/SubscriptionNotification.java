package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionNotification {
    @JsonProperty("endpoint") private SubscriptionNotificationEndpoint endpoint;
}
