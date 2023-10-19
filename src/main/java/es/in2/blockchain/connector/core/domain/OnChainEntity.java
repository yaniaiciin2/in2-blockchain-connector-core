package es.in2.blockchain.connector.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnChainEntity {

    @JsonProperty("eventType")
    private String eventType;

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
