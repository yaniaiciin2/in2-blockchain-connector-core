package es.in2.blockchainconnector.integration.dltadapter.controller;

import es.in2.blockchainconnector.core.service.OffChainService;
import es.in2.blockchainconnector.integration.dltadapter.domain.BlockchainNodeNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/notifications/dlt")
@RequiredArgsConstructor
public class DLTAdapterNotificationController {

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
