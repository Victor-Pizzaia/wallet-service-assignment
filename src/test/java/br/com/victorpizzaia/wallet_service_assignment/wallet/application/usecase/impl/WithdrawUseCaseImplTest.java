package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

class WithdrawUseCaseImplTest {

    private WalletService walletService;
    private WithdrawUseCaseImpl withdrawUseCase;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        withdrawUseCase = new WithdrawUseCaseImpl(walletService);
    }

    @Test
    void withdraw_shouldDelegateToWalletService() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("100.00");

        withdrawUseCase.withdraw(userId, amount);

        verify(walletService, times(1)).withdraw(userId, amount);
    }

    @Test
    void withdraw_shouldPassCorrectArguments() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("50.50");

        withdrawUseCase.withdraw(userId, amount);

        verify(walletService).withdraw(ArgumentMatchers.eq(userId), ArgumentMatchers.eq(amount));
    }
}
