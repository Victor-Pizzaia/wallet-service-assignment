package br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletHistoryId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletTransactionType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WALLETS_HISTORY")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class WalletHistory {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private WalletHistoryId id;

    @Embedded
    @EqualsAndHashCode.Include
    @AttributeOverride(name = "id", column = @Column(name = "wallet_id"))
    private WalletId walletId;

    @Embedded
    @EqualsAndHashCode.Include
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WalletTransactionType transactionType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public WalletHistory(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType) {
        this.id = new WalletHistoryId();
        this.userId = userId;
        this.walletId = walletId;
        this.balance = balance;
        this.amount = amount;
        this.transactionType = WalletTransactionType.valueOf(transactionType);
    }
}
