package es.in2.dome.blockchain.connector.integration.contextbroker.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockchainEventEntity {
    @JsonProperty("type")
    private String type;

    @JsonProperty("data_location")
    private String dataLocation;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("metadata")
    private BlockchainEventEntityMetadata metadata;
}
