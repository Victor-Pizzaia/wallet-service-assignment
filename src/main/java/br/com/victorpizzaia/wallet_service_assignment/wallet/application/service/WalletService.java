package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

public interface WalletService {
    public void createWallet(UserId userId);
    public BigDecimal getActualBalance(UserId userId);
    public void deposit(UserId userId, BigDecimal amount);
    public void withdraw(UserId userId, BigDecimal amount);
    public void transfer(TransactionId transactionId, UserId userId, WalletId payerId, WalletId payeeId, BigDecimal amount);
}
// ToDo - Implement depoist and withdraw return actual balance
