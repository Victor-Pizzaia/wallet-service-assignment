package br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WALLETS")
@NoArgsConstructor
public class Wallet {
    @EmbeddedId
    private WalletId id;
    
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime lastUpDateTime;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastUpDateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpDateTime = LocalDateTime.now();
    }

    public Wallet(UserId userId) {
        this.id = new WalletId();
        this.balance = BigDecimal.ZERO;
        this.userId = userId;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdraw.");
        }

        this.balance = this.balance.subtract(amount);
    }
}
