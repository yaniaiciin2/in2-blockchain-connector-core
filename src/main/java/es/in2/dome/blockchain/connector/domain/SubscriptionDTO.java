package es.in2.dome.blockchain.connector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDTO {
    @JsonProperty("entities") private List<SubscriptionEntityDTO> entities;
    @JsonProperty("id") private String id;
    @JsonProperty("type") private String type;
    @JsonProperty("notification") private SubscriptionNotificationDTO notification;
}
