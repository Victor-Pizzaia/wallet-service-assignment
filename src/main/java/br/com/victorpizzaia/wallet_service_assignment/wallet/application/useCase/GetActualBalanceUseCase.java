package br.com.victorpizzaia.wallet_service_assignment.wallet.application.useCase;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;

public interface GetActualBalanceUseCase {
    BalanceResponse getActualBalance(UserId userId);
}
