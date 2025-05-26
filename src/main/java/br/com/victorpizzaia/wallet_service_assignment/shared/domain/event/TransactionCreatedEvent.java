package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.modulith.events.Externalized;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

@Externalized
public record TransactionCreatedEvent(TransactionId transactionId, WalletId payerId, WalletId payeeKey, BigDecimal amount) implements Serializable {
}
