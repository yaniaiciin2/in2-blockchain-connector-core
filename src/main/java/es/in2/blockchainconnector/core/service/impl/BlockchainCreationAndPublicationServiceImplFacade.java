package es.in2.blockchainconnector.core.service.impl;

import es.in2.blockchainconnector.core.service.BlockchainCreationAndPublicationServiceFacade;
import es.in2.blockchainconnector.core.service.BlockchainEventCreationService;
import es.in2.blockchainconnector.core.service.BlockchainEventPublicationService;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainCreationAndPublicationServiceImplFacade implements BlockchainCreationAndPublicationServiceFacade {

    private final BlockchainEventCreationService blockchainEventCreationService;

    private final BlockchainEventPublicationService blockchainEventPublicationService;

    // Create and Publish a Blockchain Event into the Blockchain Node
    @Override
    public Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(OnChainEventDTO onChainEventDTO) {
        return blockchainEventCreationService.createBlockchainEvent(onChainEventDTO)
                .flatMap(blockchainEventPublicationService::publishBlockchainEventIntoBlockchainNode)
                .doOnSuccess(voidValue -> log.info("Blockchain event published successfully"))
                .doOnError(error -> log.error("Error creating or publishing blockchain event: {}", error.getMessage(), error));
    }

}
