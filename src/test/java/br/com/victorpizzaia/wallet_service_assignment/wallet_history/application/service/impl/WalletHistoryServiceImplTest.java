package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletTransactionType;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistory;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletHistoryServiceImplTest {

    private WalletHistoryRepository walletHistoryRepository;
    private WalletHistoryServiceImpl walletHistoryService;

    @BeforeEach
    void setUp() {
        walletHistoryRepository = mock(WalletHistoryRepository.class);
        walletHistoryService = new WalletHistoryServiceImpl(walletHistoryRepository);
    }

    @Test
    void recordWalletHistory_savesWalletHistoryWithCorrectValues() {
        UserId userId = new UserId();
        WalletId walletId = new WalletId();
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal amount = new BigDecimal("50.00");
        WalletTransactionType transactionType = WalletTransactionType.DEPOSIT;

        walletHistoryService.recordWalletHistory(userId, walletId, balance, amount, transactionType.getValue());

        ArgumentCaptor<WalletHistory> captor = ArgumentCaptor.forClass(WalletHistory.class);
        verify(walletHistoryRepository, times(1)).save(captor.capture());
        WalletHistory saved = captor.getValue();

        assertEquals(userId, saved.getUserId());
        assertEquals(walletId, saved.getWalletId());
        assertEquals(balance, saved.getBalance());
        assertEquals(amount, saved.getAmount());
        assertEquals(transactionType, saved.getTransactionType());
    }

    @Test
    void recordWalletHistory_callsRepositorySaveOnce() {
        UserId userId = new UserId();
        WalletId walletId = new WalletId();
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.TEN;
        String transactionType = "WITHDRAW";

        walletHistoryService.recordWalletHistory(userId, walletId, balance, amount, transactionType);

        verify(walletHistoryRepository, times(1)).save(any(WalletHistory.class));
    }
}
