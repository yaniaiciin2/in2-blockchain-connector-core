package es.in2.dome.blockchainconnector.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomeEventDTO {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("dataLocation")
    private String dataLocation;

    @JsonProperty("relevantMetadata")
    private List<String> metadata;

}
