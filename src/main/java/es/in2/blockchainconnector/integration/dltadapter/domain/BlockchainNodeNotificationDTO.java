package es.in2.blockchainconnector.integration.dltadapter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockchainNodeNotificationDTO {

    @JsonProperty("id")
    private BlockchainNodeNotificationIdDTO id;

    @JsonProperty("publisherAddress")
    private String publisherAddress;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("timestamp")
    private BlockchainNodeNotificationTimestampDTO timestamp;

    @JsonProperty("dataLocation")
    private String dataLocation;

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
