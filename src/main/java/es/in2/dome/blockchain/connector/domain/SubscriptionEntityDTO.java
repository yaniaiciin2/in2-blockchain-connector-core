package es.in2.dome.blockchain.connector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionEntityDTO {
    @JsonProperty("type") private String type;
}
