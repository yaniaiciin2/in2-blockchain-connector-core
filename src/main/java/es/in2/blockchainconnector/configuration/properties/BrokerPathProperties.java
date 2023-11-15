package es.in2.blockchainconnector.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

/**
 * NGSI-LD Paths
 *
 * @param entities      - entities path
 * @param subscriptions - subscriptions path
 */
public record BrokerPathProperties(String entities, String subscriptions) {

    @ConstructorBinding
    public BrokerPathProperties(String entities, String subscriptions) {
        this.entities = Optional.ofNullable(entities).orElse("/ngsi-ld/v1/entities");
        this.subscriptions = Optional.ofNullable(subscriptions).orElse("/ngsi-ld/v1/subscriptions");
    }

}
