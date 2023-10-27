package es.in2.blockchain.connector.integration.blockchainnode.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "blockchain")
public record BlockchainProperties(String rpcAddress, String userEthereumAddress,
								   @NestedConfigurationProperty BlockchainSubscriptionConfiguration subscription) {

	@ConstructorBinding
	public BlockchainProperties(String rpcAddress, String userEthereumAddress,
			BlockchainSubscriptionConfiguration subscription) {
		this.rpcAddress = rpcAddress;
		this.userEthereumAddress = userEthereumAddress;
		this.subscription = subscription;
	}
}
