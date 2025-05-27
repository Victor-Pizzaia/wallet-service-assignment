package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

public interface WalletService {
    public void createWallet(UserId userId);
    public BigDecimal getActualBalance(UserId userId);
    public BigDecimal deposit(UserId userId, BigDecimal amount);
    public BigDecimal withdraw(UserId userId, BigDecimal amount);
    public BigDecimal transaction(UserId payerId, String payeeKey, BigDecimal amount);
}
