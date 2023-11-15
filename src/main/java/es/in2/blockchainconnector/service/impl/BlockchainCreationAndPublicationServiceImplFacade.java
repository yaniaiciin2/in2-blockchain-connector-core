package es.in2.blockchainconnector.service.impl;

import es.in2.blockchainconnector.service.BlockchainCreationAndPublicationServiceFacade;
import es.in2.blockchainconnector.service.BlockchainEventCreationService;
import es.in2.blockchainconnector.service.BlockchainEventPublicationService;
import es.in2.blockchainconnector.domain.OnChainEventDTO;
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

    @Override
    public Mono<Void> createAndPublishABlockchainEventIntoBlockchainNode(OnChainEventDTO onChainEventDTO) {
        return blockchainEventCreationService.createBlockchainEvent(onChainEventDTO)
                .flatMap(blockchainEventPublicationService::publishBlockchainEventIntoBlockchainNode)
                .doOnSuccess(voidValue -> log.info("Blockchain Event published successfully"))
                .doOnError(error -> log.error("Error creating or publishing Blockchain Event: {}", error.getMessage(), error));
    }

}
