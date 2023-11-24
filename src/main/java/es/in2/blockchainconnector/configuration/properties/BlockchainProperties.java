package es.in2.blockchainconnector.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

/**
 * Configuration to connect the blockchain
 *
 * @param rpcAddress          - rpc address of the node to be used
 * @param userEthereumAddress - address of the user in the ethereum compatible blockchain
 * @param subscription        - configuration to be used for subscribing at blockchain events
 */
@ConfigurationProperties(prefix = "blockchain")
public record BlockchainProperties(String rpcAddress, String userEthereumAddress,
                                   @NestedConfigurationProperty BlockchainSubscriptionConfigProperties subscription) {

    @ConstructorBinding
    public BlockchainProperties(String rpcAddress, String userEthereumAddress,
                                BlockchainSubscriptionConfigProperties subscription) {
        this.rpcAddress = rpcAddress;
        this.userEthereumAddress = userEthereumAddress;
        this.subscription = Optional.ofNullable(subscription).orElse(new BlockchainSubscriptionConfigProperties(null, false, null));
    }

}
