package es.in2.blockchainconnector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BlockchainNodeDTO(
        @JsonProperty("rpcAddress") String rpcAddress,
        @JsonProperty("userEthereumAddress") String userEthereumAddress
) {
}
