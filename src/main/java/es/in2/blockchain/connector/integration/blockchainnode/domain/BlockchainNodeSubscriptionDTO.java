package es.in2.blockchain.connector.integration.blockchainnode.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainNodeSubscriptionDTO {

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("notificationEndpoint")
    private String notificationEndpoint;

}
