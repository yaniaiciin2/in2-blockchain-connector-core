package es.in2.blockchainconnector.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

public record BrokerAdapterPathProperties(String subscribe, String publish, String update) {

    @ConstructorBinding
    public BrokerAdapterPathProperties(String subscribe, String publish, String update) {
        this.subscribe = Optional.ofNullable(subscribe).orElse("/api/v1/subscribe");
        this.publish = Optional.ofNullable(publish).orElse("/api/v1/publish");
        this.update = Optional.ofNullable(update).orElse("/api/v1/update");
    }

}