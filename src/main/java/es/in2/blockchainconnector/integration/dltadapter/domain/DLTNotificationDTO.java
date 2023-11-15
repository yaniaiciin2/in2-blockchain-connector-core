package es.in2.blockchainconnector.integration.dltadapter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record DLTNotificationDTO(
        @JsonProperty("id") BlockchainNodeNotificationIdDTO id,
        @JsonProperty("publisherAddress") String publisherAddress,
        @JsonProperty("eventType") String eventType,
        @JsonProperty("timestamp") BlockchainNodeNotificationTimestampDTO timestamp,
        @JsonProperty("dataLocation") String dataLocation,
        @JsonProperty("relevantMetadata") List<String> relevantMetadata
) {
}
