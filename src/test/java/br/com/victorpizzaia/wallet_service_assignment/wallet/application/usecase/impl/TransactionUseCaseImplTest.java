package br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

class TransactionUseCaseImplTest {

    private WalletService walletService;
    private TransactionUseCaseImpl transactionUseCase;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        transactionUseCase = new TransactionUseCaseImpl(walletService);
    }

    @Test
    void transaction_shouldDelegateToWalletService() {
        UserId userId = new UserId(UUID.randomUUID());
        String payeeKey = "payeeKey";
        BigDecimal amount = new BigDecimal(100.0);
        WalletTransactionRequest request = new WalletTransactionRequest("payeeKey", new BigDecimal(100));

        transactionUseCase.transaction(userId, request);

        verify(walletService, times(1)).transaction(userId, payeeKey, amount);
    }
}
