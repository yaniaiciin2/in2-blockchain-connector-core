package es.in2.blockchain.connector.integration.blockchainnode.configuration;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;
import java.util.Optional;

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
