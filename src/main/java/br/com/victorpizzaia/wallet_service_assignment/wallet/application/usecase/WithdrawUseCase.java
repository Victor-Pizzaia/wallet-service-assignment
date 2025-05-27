package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;

public interface WithdrawUseCase {
    BalanceResponse withdraw(UserId userId, BigDecimal amount);
}
