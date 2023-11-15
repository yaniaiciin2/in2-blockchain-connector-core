package es.in2.blockchainconnector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BlockchainNodeNotificationIdDTO(
        @JsonProperty("type") String type,
        @JsonProperty("hex") String hex
) {
}
