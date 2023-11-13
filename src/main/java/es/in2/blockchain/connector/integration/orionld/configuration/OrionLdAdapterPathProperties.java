package es.in2.blockchain.connector.integration.orionld.configuration;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

public record OrionLdAdapterPathProperties(String subscribe, String publish, String update) {

	@ConstructorBinding
	public OrionLdAdapterPathProperties(String subscribe, String publish, String update) {
		this.subscribe = Optional.ofNullable(subscribe).orElse("/api/v1/subscribe");
		this.publish = Optional.ofNullable(publish).orElse("/api/v1/publish");
		this.update = Optional.ofNullable(update).orElse("/api/v1/update");
	}
}