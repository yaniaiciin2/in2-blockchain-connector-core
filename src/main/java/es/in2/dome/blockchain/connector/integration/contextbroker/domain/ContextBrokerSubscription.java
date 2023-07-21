package es.in2.dome.blockchain.connector.integration.contextbroker.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContextBrokerSubscription {
    @JsonProperty("entities")
    private List<SubscriptionEntity> entities;
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("notification")
    private SubscriptionNotification notification;
}
