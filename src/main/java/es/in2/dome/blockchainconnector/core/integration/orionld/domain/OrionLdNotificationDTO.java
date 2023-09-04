package es.in2.dome.blockchainconnector.core.integration.orionld.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrionLdNotificationDTO {

    @JsonProperty("id")
    String id;

    @JsonProperty("type")
    String type;

    @JsonProperty("data")
    String data;

    @JsonProperty("subscriptionId")
    String subscriptionId;

    @JsonProperty("notifiedAt")
    String notifiedAt;

}
