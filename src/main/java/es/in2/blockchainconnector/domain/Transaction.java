package es.in2.blockchainconnector.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("transactions")
public class Transaction {

    @Id
    @Column("id")
    private UUID id;

    @Column("created_at")
    private Timestamp createdAt;

    @Column("data_location")
    private String dataLocation;

    @Column("entity_id")
    private String entityId;

    @Column("entity_hash")
    private String entityHash;

    @Column("status")
    private TransactionStatus status;

    @Column("trader")
    private TransactionTrader trader;

    @Column("hash")
    private String hash;
    
}
