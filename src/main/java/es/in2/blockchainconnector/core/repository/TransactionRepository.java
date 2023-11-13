package es.in2.blockchainconnector.core.repository;

import es.in2.blockchainconnector.core.domain.Transaction;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends RevisionRepository<Transaction, UUID, Integer> {
    void save(Transaction transaction);
    Transaction findById(UUID uuid);
}
