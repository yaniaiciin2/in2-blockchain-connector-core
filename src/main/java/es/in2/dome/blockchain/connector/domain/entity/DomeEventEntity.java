package es.in2.dome.blockchain.connector.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomeEventEntity {

    @JsonProperty("type")
    private String type;

    @JsonProperty("data_location")
    private String dataLocation;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("metadata")
    private List<String> metadata;
}
