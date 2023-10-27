package es.in2.blockchain.connector.integration.orionld.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

@ConfigurationProperties(prefix = "dlt-adapter")
public record DLTAdapterProperties(String domain, @NestedConfigurationProperty DLTAdapterPathProperties paths) {

	@ConstructorBinding
	public DLTAdapterProperties(String domain, DLTAdapterPathProperties paths) {
		this.domain = domain;
		this.paths = Optional.ofNullable(paths).orElse(new DLTAdapterPathProperties(null, null, null));
	}
}
