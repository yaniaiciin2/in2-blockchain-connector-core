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

        String dataLocation = blockchainNodeNotificationDTO.getDataLocation();
        log.debug(" > Data Location: {}", dataLocation);

        String retrievedEntity = hashLinkService.resolveHashlink(dataLocation);

        if (entityExists(retrievedEntity)) {
            String entityId = applicationUtils.jsonExtractId(retrievedEntity);
            String orionLdEntitiesUrl = buildOrionLdEntityUrl(entityId);
            log.debug("> Orion-LD Existing Entity URL: " + orionLdEntitiesUrl);

            String existingEntity = applicationUtils.getRequest(orionLdEntitiesUrl);

            if (!areEntitiesEqual(retrievedEntity, existingEntity)) {
                log.debug("> Entities not equal");
                //TODO COMPROBAR DIFERENCIAS JSON
                
                //TODO CREAR JSON PARA HACER PATCH REQUEST


                // Patch Request
                applicationUtils.patchRequest(orionLdEntitiesUrl, retrievedEntity);
                log.info("  > Entity updated in Orion-LD");
            } else {
                log.info("> Same entities. No changes.");
            }
        } else {
            publishEntityToDestinationOffChain(retrievedEntity);
            log.info("  > Entity published to off-chain");
        }
    }

    private String buildOrionLdEntityUrl(String entityId) {
        return orionLdProperties.getOrionLdDomain() + orionLdProperties.getOrionLdPathEntities() + "/" + entityId;
    }


    private void publishEntityToDestinationOffChain(String retrievedEntity) {
        String orionLdEntitiesUrl = orionLdProperties.getApiDomain() + orionLdProperties.getApiPathEntities();
        log.debug(" > Publishing entity to: {}", orionLdEntitiesUrl);
        log.debug(" > Entity to publish: {}", retrievedEntity);
        applicationUtils.postRequest(orionLdEntitiesUrl, retrievedEntity);
    }

    private boolean entityExists(String retrievedEntity) {
        String entityId = applicationUtils.jsonExtractId(retrievedEntity);
        log.debug("> id: " +entityId);
        String orionLdEntitiesUrl = orionLdProperties.getOrionLdDomain() + orionLdProperties.getOrionLdPathEntities() + "/"
                + entityId;
        log.debug("> orionURL: " +orionLdEntitiesUrl);
        String statusCode = applicationUtils.getRequestCode(orionLdEntitiesUrl);

        return statusCode.equals("200");
    }

    private boolean areEntitiesEqual(String datalocation, String existingEntity) {
        return hashLinkService.compareHashLinks(datalocation, existingEntity);
    }

}