package es.in2.blockchain.connector.integration.orionld.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

/**
 * Configuration to connect with the orion-ld-adapter.
 * @param domain - domain of the adapter
 * @param paths - paths to be used with that adapter instance
 */
@ConfigurationProperties(prefix = "orion-ld-adapter")
public record OrionLdAdapterProperties(String domain, @NestedConfigurationProperty OrionLdAdapterPathProperties paths) {

	@ConstructorBinding
	public OrionLdAdapterProperties(String domain, OrionLdAdapterPathProperties paths) {
		this.domain = domain;
		this.paths = Optional.ofNullable(paths).orElse(new OrionLdAdapterPathProperties(null, null, null));
	}
}

