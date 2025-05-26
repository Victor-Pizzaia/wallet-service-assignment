package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.modulith.events.Externalized;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

@Externalized
public record WalletUpdatedEvent(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String walletTransactionType) implements Serializable {
}
