package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

public record ExecuteTransactionEvent(TransactionId transactionId, WalletId payerId, WalletId payeeId, BigDecimal amount) {

}
