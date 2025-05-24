package br.com.victorpizzaia.wallet_service_assignment.wallet.application.useCase.impl;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.useCase.GetActualBalanceUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;

@UseCase
public class GetActualBalanceUseCaseImpl implements GetActualBalanceUseCase {

    private final WalletRepository walletRepository;

    public GetActualBalanceUseCaseImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public BalanceResponse getActualBalance(UserId userId) {
        BigDecimal balance = walletRepository.findBalanceByUserId(userId).orElseThrow();
        return new BalanceResponse(balance);
    }
}
