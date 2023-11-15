package es.in2.blockchainconnector.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record OnChainEvent(
        @JsonProperty("eventType") String eventType,
        @JsonProperty("iss") String organizationId,
        @JsonProperty("dataLocation") String dataLocation,
        @JsonProperty("relevantMetadata") List<String> metadata
) {
}
