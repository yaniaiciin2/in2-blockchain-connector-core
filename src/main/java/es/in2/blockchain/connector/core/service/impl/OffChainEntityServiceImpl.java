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
        // retrieve entity from origin off-chain using dataLocation
        String dataLocation = blockchainNodeNotificationDTO.getDataLocation();
        log.debug("Retrieving entity from dataLocation: {}", dataLocation);
        // verify entity with hashlink
        String retrievedEntity = hashLinkService.resolveHashlink(dataLocation);
        // publish retrieved entity to our off-chain storage
        publishEntityToDestinationOffChain(retrievedEntity);
    }

    private void publishEntityToDestinationOffChain(String retrievedEntity) {
        String orionLdEntitiesUrl = orionLdProperties.getApiDomain() + orionLdProperties.getOrionLdPathEntities();
        applicationUtils.postRequest(orionLdEntitiesUrl, retrievedEntity);
    }

}
