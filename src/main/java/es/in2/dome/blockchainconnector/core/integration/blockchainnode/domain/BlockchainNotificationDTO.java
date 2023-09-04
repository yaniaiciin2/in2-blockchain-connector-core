package es.in2.dome.blockchainconnector.core.integration.blockchainnode.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockchainNotificationDTO {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("dataLocation")
    private String dataLocation;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("relevantMetadata")
    private List<String> metadata;

}
