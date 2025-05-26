package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepositUseCaseImplTest {

    private WalletService walletService;
    private DepositUseCaseImpl depositUseCase;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        depositUseCase = new DepositUseCaseImpl(walletService);
    }

    @Test
    void deposit_shouldDelegateToWalletService() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("100.00");

        depositUseCase.deposit(userId, amount);

        verify(walletService, times(1)).deposit(userId, amount);
    }

    @Test
    void deposit_shouldPassCorrectArguments() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("250.50");

        depositUseCase.deposit(userId, amount);

        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        verify(walletService).deposit(userIdCaptor.capture(), amountCaptor.capture());

        assertEquals(userId, userIdCaptor.getValue());
        assertEquals(amount, amountCaptor.getValue());
    }
}
