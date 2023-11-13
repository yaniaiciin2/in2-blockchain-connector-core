package es.in2.blockchainconnector.integration.dltadapter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockchainNodeNotificationIdDTO {

    @JsonProperty("type")
    private String type;

    @JsonProperty("hex")
    private String hex;

}
