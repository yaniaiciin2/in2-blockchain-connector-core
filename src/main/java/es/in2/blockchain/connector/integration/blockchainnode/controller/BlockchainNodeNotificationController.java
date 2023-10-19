package es.in2.blockchain.connector.integration.blockchainnode.controller;

import es.in2.blockchain.connector.core.service.OffChainService;
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

    private final OffChainService offChainService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> blockchainNodeNotification(@RequestBody Mono<BlockchainNodeNotificationDTO> blockchainNodeNotificationDTO) {
        log.debug(">>> Notification received: {}", blockchainNodeNotificationDTO.toString());
        return blockchainNodeNotificationDTO
                .flatMap(dto -> {
                    offChainService.retrieveAndPublishEntityToOffChain(dto);
                    return Mono.empty();
                });
    }

}
