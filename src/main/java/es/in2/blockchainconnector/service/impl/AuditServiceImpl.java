//package es.in2.blockchainconnector.core.service.impl;
//
//import es.in2.blockchainconnector.core.domain.Transaction;
//import es.in2.blockchainconnector.core.repository.TransactionRepository;
//import es.in2.blockchainconnector.core.service.AuditService;
//import es.in2.blockchainconnector.core.service.HashLinkService;
//import es.in2.blockchainconnector.core.utils.AuditStatus;
//import es.in2.blockchainconnector.domain.OnChainEventDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.NoSuchElementException;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class AuditServiceImpl implements AuditService {
//
//    private final TransactionRepository transactionRepository;
//    private final HashLinkService hashLinkService;
//
//    @Override
//    public Mono<Transaction> createTransaction(OnChainEventDTO onChainEventDTO) {
//        return Mono.fromCallable(() -> {
//            String id = onChainEventDTO.id();
//            Transaction transaction = Transaction.builder()
//                    .entityId(id)
//                    .entityHash("")
//                    .dataLocation(String.valueOf(hashLinkService.createHashLink(id, onChainEventDTO.data())))
//                    .status(AuditStatus.RECEIVED.getDescription())
//                    .build();
//            saveTransaction(transaction);
//            return transaction;
//        });
//    }
//
//    @Override
//    public Mono<Void> updateTransaction(Transaction transaction) {
//        return findTransactionById(transaction.getId())
//                .switchIfEmpty(Mono.error(new NoSuchElementException("Transaction not found.")))
//                .flatMap(existingTransaction -> {
//                    saveTransaction(transaction);
//                    return Mono.empty();
//                });
//    }
//
//
//
//
//    private Mono<Transaction> findTransactionById(UUID id) {
//        return Mono.fromCallable(() -> transactionRepository.findById(id));
//    }
//
//    private void saveTransaction(Transaction transaction) {
//        transactionRepository.save(transaction);
//    }
//}
