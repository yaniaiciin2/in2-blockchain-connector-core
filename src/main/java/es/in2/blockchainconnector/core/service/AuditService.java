package es.in2.blockchainconnector.core.service;

import es.in2.blockchainconnector.core.domain.Transaction;
import es.in2.blockchainconnector.integration.brokeradapter.domain.OnChainEntityDTO;
import reactor.core.publisher.Mono;

public interface AuditService {

    Mono<Transaction> createTransaction(OnChainEntityDTO onChainEntityDTO);
    Mono<Void> updateTransaction(Transaction transaction);

}
