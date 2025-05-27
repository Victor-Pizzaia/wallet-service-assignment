package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletTransactionRequest;

@UseCase
public class TransactionUseCaseImpl implements TransactionUseCase {

    private final WalletService walletService;

    public TransactionUseCaseImpl(WalletService walletService) {
        this.walletService = walletService;
    }

	@Override
	public BalanceResponse transaction(UserId userId, WalletTransactionRequest request) {
		return new BalanceResponse(walletService.transaction(userId, request.payeeKey(), request.amount()));
	}
}
