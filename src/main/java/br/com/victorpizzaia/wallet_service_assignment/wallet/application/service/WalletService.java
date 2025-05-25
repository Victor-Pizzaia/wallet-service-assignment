package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

public interface WalletService {
    public BigDecimal getActualBalance(UserId userId);
    public void deposit(UserId userId, BigDecimal amount);
    public void withdraw(UserId userId, BigDecimal amount);
}
