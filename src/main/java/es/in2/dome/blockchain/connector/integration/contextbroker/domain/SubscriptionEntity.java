package es.in2.dome.blockchain.connector.integration.contextbroker.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionEntity {
    @JsonProperty("type") private String type;
}
