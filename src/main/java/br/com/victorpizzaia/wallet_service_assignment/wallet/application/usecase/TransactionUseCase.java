package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletTransactionRequest;

public interface TransactionUseCase {
    BalanceResponse transaction(UserId userId, WalletTransactionRequest request);
}
