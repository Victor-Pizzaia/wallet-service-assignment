package br.com.victorpizzaia.wallet_service_assignment.transaction.domain;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.PositiveAmount;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import jakarta.validation.constraints.NotNull;

public record CreateTransactionRequest(@NotNull WalletId payerId, @NotNull WalletId payeeId, @PositiveAmount @NotNull BigDecimal amount) {
}
