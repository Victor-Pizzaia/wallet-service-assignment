package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.listener;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.WalletUpdatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

class WalletEventListenerTest {

    private WalletHistoryService walletHistoryService;
    private WalletEventListener walletEventListener;

    @BeforeEach
    void setUp() {
        walletHistoryService = mock(WalletHistoryService.class);
        walletEventListener = new WalletEventListener(walletHistoryService);
    }

    @Test
    void onWalletUpdated_shouldCallRecordWalletHistoryWithCorrectArguments() {
        UserId userId = new UserId();
        WalletId walletId = new WalletId();
        BigDecimal balance = new BigDecimal(100);
        BigDecimal amount = new BigDecimal(50);
        String walletTransactionType = "DEPOSIT";

        WalletUpdatedEvent event = mock(WalletUpdatedEvent.class);
        when(event.userId()).thenReturn(userId);
        when(event.walletId()).thenReturn(walletId);
        when(event.balance()).thenReturn(balance);
        when(event.amount()).thenReturn(amount);
        when(event.walletTransactionType()).thenReturn(walletTransactionType);

        walletEventListener.onWalletUpdated(event);

        verify(walletHistoryService, times(1)).recordWalletHistory(
                eq(userId),
                eq(walletId),
                eq(balance),
                eq(amount),
                eq(walletTransactionType)
        );
    }
}
