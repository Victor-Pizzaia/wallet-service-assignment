package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.WithdrawUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;

@UseCase
public class WithdrawUseCaseImpl implements WithdrawUseCase {

    private final WalletService walletService;

    public WithdrawUseCaseImpl(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public BalanceResponse withdraw(UserId userId, BigDecimal amount) {
        return new BalanceResponse(walletService.withdraw(userId, amount));
    }
}
