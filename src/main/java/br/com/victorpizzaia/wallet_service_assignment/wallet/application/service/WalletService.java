package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.TransactionCreatedResponse;

public interface WalletService {
    public void createWallet(UserId userId);
    public BigDecimal getActualBalance(UserId userId);
    public BigDecimal deposit(UserId userId, BigDecimal amount);
    public BigDecimal withdraw(UserId userId, BigDecimal amount);
    public TransactionCreatedResponse transaction(UserId payerId, String payeeKey, BigDecimal amount);
    public void executeTransaction(TransactionId transactionId, WalletId payerId, WalletId payeeId, BigDecimal amount);
}
