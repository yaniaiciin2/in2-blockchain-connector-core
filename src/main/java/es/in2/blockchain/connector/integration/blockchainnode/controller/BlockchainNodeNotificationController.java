package es.in2.blockchain.connector.integration.blockchainnode.controller;

import es.in2.blockchain.connector.core.service.OffChainEntityService;
import es.in2.blockchain.connector.integration.blockchainnode.domain.BlockchainNodeNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/notifications/blockchain-node")
@RequiredArgsConstructor
public class BlockchainNodeNotificationController {

    private final OffChainEntityService offChainEntityService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> blockchainNodeNotification(@RequestBody BlockchainNodeNotificationDTO blockchainNodeNotificationDTO) {
        log.debug(">>> Notification received: {}", blockchainNodeNotificationDTO.toString());

        return Mono.defer(() -> {
            offChainEntityService.retrieveAndPublishEntityToOffChain(blockchainNodeNotificationDTO);
            return Mono.empty();
        });
    }


}
