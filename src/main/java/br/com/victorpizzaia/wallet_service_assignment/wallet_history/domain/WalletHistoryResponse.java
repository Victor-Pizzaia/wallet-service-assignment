package br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistory;

public record WalletHistoryResponse(BigDecimal balance, BigDecimal amount, WalletTransactionType transactionType, LocalDateTime createdAt) {

    public static WalletHistoryResponse toResponse(WalletHistory entity) {
        return new WalletHistoryResponse(
            entity.getBalance(),
            entity.getAmount(),
            entity.getTransactionType(),
            entity.getCreatedAt()
        );
    }
}
