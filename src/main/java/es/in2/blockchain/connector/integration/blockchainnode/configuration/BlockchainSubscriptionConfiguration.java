package es.in2.blockchain.connector.integration.blockchainnode.configuration;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;
import java.util.Optional;

/**
 * Configuration to be used for subscribing at the blockchain
 * @param notificationEndpoint - endpoint to notify on events
 * @param active - should the subscription be active
 * @param eventTypes - type of events to subscribe to
 */
public record BlockchainSubscriptionConfiguration(String notificationEndpoint, boolean active,
												  List<String> eventTypes) {

	@ConstructorBinding
	public BlockchainSubscriptionConfiguration(String notificationEndpoint, boolean active,
			List<String> eventTypes) {
		this.notificationEndpoint = notificationEndpoint;
		this.active = active;
		this.eventTypes = Optional.ofNullable(eventTypes).orElse(List.of());
	}
}
