package es.in2.blockchain.connector.core.service.impl;

import es.in2.blockchain.connector.core.service.HashLinkService;
import es.in2.blockchain.connector.core.service.OffChainEntityService;
import es.in2.blockchain.connector.core.utils.ApplicationUtils;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import es.in2.blockchain.connector.integration.orionld.configuration.OrionLdProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffChainEntityServiceImpl implements OffChainEntityService {

    private final HashLinkService hashLinkService;
    private final ApplicationUtils applicationUtils;
    private final OrionLdProperties orionLdProperties;

    @Override
    public void retrieveAndPublishEntityToOffChain(BlockchainNodeNotificationDTO blockchainNodeNotificationDTO) {
        log.info(">>> Retrieving and publishing entity to off-chain...");
        // retrieve entity from origin off-chain using dataLocation
        String dataLocation = blockchainNodeNotificationDTO.getDataLocation();
        log.debug(" > Data Location: {}", dataLocation);
        // verify entity with hashlink
        String retrievedEntity = hashLinkService.resolveHashlink(dataLocation);
        // publish retrieved entity to our off-chain storage
        publishEntityToDestinationOffChain(retrievedEntity);
        log.info("  > Entity published to off-chain");
    }

    private void publishEntityToDestinationOffChain(String retrievedEntity) {
        String orionLdEntitiesUrl = orionLdProperties.getApiDomain() + orionLdProperties.getApiPathEntities();
        log.debug(" > Publishing entity to: {}", orionLdEntitiesUrl);
        log.debug(" > Entity to publish: {}", retrievedEntity);
        applicationUtils.postRequest(orionLdEntitiesUrl, retrievedEntity);
    }

}
