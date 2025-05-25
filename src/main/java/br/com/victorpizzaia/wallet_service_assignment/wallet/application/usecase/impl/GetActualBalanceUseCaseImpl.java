package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.GetActualBalanceUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;

@UseCase
public class GetActualBalanceUseCaseImpl implements GetActualBalanceUseCase {

    private final WalletService walletService;

    public GetActualBalanceUseCaseImpl(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public BalanceResponse getActualBalance(UserId userId) {
        return new BalanceResponse(walletService.getActualBalance(userId));
    }
}
