package es.in2.dome.blockchainconnector.core.integration.blockchainnode.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BlockchainNodeApiConfig {

    @Value("${blockchain-node.api.domain}")
    private String apiDomain;

    @Value("${blockchain-node.api.path.configure-node}")
    private String apiPathConfigureNode;

    @Value("${blockchain-node.api.path.publish}")
    private String apiPathPublish;

    @Value("${blockchain-node.api.path.subscribe}")
    private String apiPathSubscribe;

    @Value("${blockchain-node.subscription.notification-endpoint-uri}")
    private String subscriptionNotificationEndpointUri;

    @Value("${blockchain-node.subscription.event-type}")
    private String subscriptionEventType;

    @Value("${blockchain-node.node.rpcAddress}")
    private String nodeRpcAddress;

    @Value("${blockchain-node.node.publicKeyHex}")
    private String nodePublicKeyHex;

}
