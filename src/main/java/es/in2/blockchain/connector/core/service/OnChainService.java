package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import reactor.core.publisher.Mono;

public interface OnChainService {
    Mono<Void> publishEntityToOnChainSystem(OnChainEntityDTO onChainEntityDTO);
}
