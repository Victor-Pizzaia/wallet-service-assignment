package br.com.victorpizzaia.wallet_service_assignment.wallet.domain;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.PositiveAmount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WalletTransactionRequest(@NotNull @NotBlank String payeeKey, @PositiveAmount BigDecimal amount) {
}
