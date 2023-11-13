package es.in2.blockchainconnector.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnChainEntity {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("iss")
    private String organizationId;

    @JsonProperty("dataLocation")
    private String dataLocation;

    @JsonProperty("relevantMetadata")
    private List<String> metadata;

    @Override
    public String toString() {
        return "DOME Event {" +
                "eventType = " + eventType +
                ", dataLocation = " + dataLocation +
                ", metadata = " + metadata + "}";
    }

}
