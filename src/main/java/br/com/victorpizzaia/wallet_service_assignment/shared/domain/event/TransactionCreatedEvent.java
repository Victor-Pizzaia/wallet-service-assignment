package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

public record TransactionCreatedEvent(TransactionId transactionId, UserId userId, WalletId payerId, WalletId payeeId, BigDecimal amount) {
}
