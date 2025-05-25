package br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.PositiveAmount;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRANSACTIONS")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private TransactionId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "payer_id"))
    private WalletId payerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "payee_id"))
    private WalletId payeeId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column
    private String details;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Transaction(TransactionId id, WalletId payerId, WalletId payeeId, @PositiveAmount BigDecimal amount) {
        if (payerId.equals(payeeId)) {
            throw new IllegalArgumentException("Payer and payee cannot be the same wallet");
        }

        this.id = id;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
    }

    public void complete() {
        this.status = TransactionStatus.COMPLETED;
    }

    public void fail(String message) {
        this.status = TransactionStatus.FAILED;
        this.details = message;
    }
}
