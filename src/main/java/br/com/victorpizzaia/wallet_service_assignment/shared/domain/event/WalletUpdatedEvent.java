package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

public record WalletUpdatedEvent(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String walletTransactionType) {
}
