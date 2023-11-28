package es.in2.blockchainconnector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.util.Json;
import lombok.Builder;

import java.util.List;

@Builder
public record OnChainEvent(
        @JsonProperty("eventType") String eventType,
        @JsonProperty("iss") String organizationId,
        @JsonProperty("dataLocation") String dataLocation,
        @JsonProperty("relevantMetadata") List<String> metadata,
        @JsonProperty("entityId") String entityId
) {
}
