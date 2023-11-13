package es.in2.blockchainconnector.integration.brokeradapter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

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
    List<Map<String, Object>> data;

    @JsonProperty("subscriptionId")
    String subscriptionId;

    @JsonProperty("notifiedAt")
    String notifiedAt;

    @Override
    public String toString() {
        return "Orion-LD Notification {" +
                "id = " + id +
                ", type = " + type +
                ", data = " + data +
                ", subscriptionId = " + subscriptionId +
                ", notifiedAt = " + notifiedAt +
                '}';
    }

}
