package es.in2.blockchainconnector.integration.brokeradapter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record BrokerNotificationDTO(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("data") List<Map<String, Object>> data,
        @JsonProperty("subscriptionId") String subscriptionId,
        @JsonProperty("notifiedAt") String notifiedAt
) {
}