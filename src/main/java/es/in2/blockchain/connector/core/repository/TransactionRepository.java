package es.in2.blockchain.connector.core.repository;

import es.in2.blockchain.connector.core.domain.Transaction;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.UUID;

public interface TransactionRepository extends RevisionRepository<Transaction, UUID, Integer> {
    Transaction save(Transaction transaction);
    Transaction findById(UUID uuid);
}
