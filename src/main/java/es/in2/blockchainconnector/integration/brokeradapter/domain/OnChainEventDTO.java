package es.in2.blockchainconnector.integration.brokeradapter.domain;

import lombok.Builder;

import java.util.Map;

@Builder
public record OnChainEventDTO(
        String id,
        String eventType,
        Map<String, Object> dataMap,
        String data
) {
}
