package es.in2.blockchain.connector.integration.orionld.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class OrionLdProperties {

    @Value("${orion-ld-if.orion-ld.domain}")
    private String orionLdDomain;

    @Value("${orion-ld-if.orion-ld.path.entities}")
    private String orionLdPathEntities;

    @Value("${orion-ld-if.api.domain}")
    private String apiDomain;

    @Value("${orion-ld-if.api.path.subscription}")
    private String apiPathSubscription;

    @Value("${orion-ld-if.api.path.entities}")
    private String apiPathEntities;

    @Value("{orion-ld-if.api.path.update}")
    private String apiPathUpdate;

    @Value("${orion-ld-if.subscription.type}")
    private String subscriptionType;

    @Value("${orion-ld-if.subscription.notification-endpoint-uri}")
    private String subscriptionNotificationEndpointUri;

    @Value("${orion-ld-if.subscription.entities}")
    private List<String> subscriptionEntities;

    @Value("${orion-ld-if.subscription.id-prefix}")
    private String subscriptionIdPrefix;

}
