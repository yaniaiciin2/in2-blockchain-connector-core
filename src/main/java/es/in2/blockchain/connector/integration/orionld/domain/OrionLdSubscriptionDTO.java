package es.in2.blockchain.connector.integration.orionld.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrionLdSubscriptionDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("notification-endpoint-uri")
    private String notificationEndpointUri;

    @JsonProperty("entities")
    private List<String> entities;

    @Override
    public String toString() {
        return "Orion-LD Subscription {" +
                "id =" + id +
                ", type = " + type +
                ", notificationEndpointUri = " + notificationEndpointUri +
                ", entities = " + entities +
                '}';
    }

}
