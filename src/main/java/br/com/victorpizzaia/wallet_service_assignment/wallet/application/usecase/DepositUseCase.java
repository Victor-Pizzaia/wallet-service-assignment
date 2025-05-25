package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

public interface DepositUseCase {
    void deposit(UserId userId, BigDecimal amount);
}
