package es.in2.blockchain.connector.core.service;

import es.in2.blockchain.connector.core.domain.Transaction;
import es.in2.blockchain.connector.integration.orionld.domain.OnChainEntityDTO;
import reactor.core.publisher.Mono;

public interface AuditService {

    Mono<Transaction> createTransaction(OnChainEntityDTO onChainEntityDTO);
    Mono<Void> updateTransaction(Transaction transaction);

}
