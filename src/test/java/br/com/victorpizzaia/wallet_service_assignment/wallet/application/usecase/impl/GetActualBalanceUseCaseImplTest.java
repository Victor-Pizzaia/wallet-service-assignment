package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetActualBalanceUseCaseImplTest {

    private WalletService walletService;
    private GetActualBalanceUseCaseImpl getActualBalanceUseCase;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        getActualBalanceUseCase = new GetActualBalanceUseCaseImpl(walletService);
    }

    @Test
    void shouldReturnBalanceResponseWithCorrectBalance() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal expectedBalance = new BigDecimal("100.50");
        when(walletService.getActualBalance(userId)).thenReturn(expectedBalance);

        BalanceResponse response = getActualBalanceUseCase.getActualBalance(userId);

        assertNotNull(response);
        assertEquals(expectedBalance, response.balance());
        verify(walletService, times(1)).getActualBalance(userId);
    }

    @Test
    void shouldReturnZeroBalanceWhenWalletServiceReturnsZero() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal expectedBalance = BigDecimal.ZERO;
        when(walletService.getActualBalance(userId)).thenReturn(expectedBalance);

        BalanceResponse response = getActualBalanceUseCase.getActualBalance(userId);

        assertNotNull(response);
        assertEquals(expectedBalance, response.balance());
        verify(walletService, times(1)).getActualBalance(userId);
    }

    @Test
    void shouldPropagateExceptionFromWalletService() {
        UserId userId = new UserId(UUID.randomUUID());
        when(walletService.getActualBalance(userId)).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> getActualBalanceUseCase.getActualBalance(userId));
        verify(walletService, times(1)).getActualBalance(userId);
    }
}