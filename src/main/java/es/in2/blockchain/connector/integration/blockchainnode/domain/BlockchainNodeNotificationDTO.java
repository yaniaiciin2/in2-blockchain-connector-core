package es.in2.blockchain.connector.integration.blockchainnode.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockchainNodeNotificationDTO {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("dataLocation")
    private String dataLocation;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("relevantMetadata")
    private List<String> relevantMetadata;

    @Override
    public String toString() {
        return "Blockchain Node Notification {" +
                "eventType = " + eventType +
                ", dataLocation = " + dataLocation +
                ", timestamp = " + timestamp +
                ", metadata = " + relevantMetadata +
                '}';
    }

}
