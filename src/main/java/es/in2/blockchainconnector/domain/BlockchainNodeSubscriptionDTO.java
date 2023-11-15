package es.in2.blockchainconnector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record BlockchainNodeSubscriptionDTO(
        @JsonProperty("eventTypes") List<String> eventTypeList,
        @JsonProperty("notificationEndpoint") String notificationEndpoint
) {
}
