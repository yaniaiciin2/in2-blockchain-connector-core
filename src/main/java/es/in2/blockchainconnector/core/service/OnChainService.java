package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import reactor.core.publisher.Mono;

public interface OnChainService {
    Mono<Void> publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO);
}
