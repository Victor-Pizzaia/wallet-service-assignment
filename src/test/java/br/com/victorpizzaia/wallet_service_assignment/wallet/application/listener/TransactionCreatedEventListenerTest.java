package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionCreatedEventListenerTest {

    private WalletService walletService;
    private TransactionCreatedEventListener listener;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        listener = new TransactionCreatedEventListener(walletService);
    }

    @Test
    void onTransactionCreated_shouldCallWalletServiceTransferWithCorrectArguments() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("100.00");

        TransactionCreatedEvent event = mock(TransactionCreatedEvent.class);
        when(event.transactionId()).thenReturn(transactionId);
        when(event.userId()).thenReturn(userId);
        when(event.payerId()).thenReturn(payerId);
        when(event.payeeId()).thenReturn(payeeId);
        when(event.amount()).thenReturn(amount);

        listener.onTransactionCreated(event);

        verify(walletService, times(1)).transfer(
                eq(transactionId),
                eq(userId),
                eq(payerId),
                eq(payeeId),
                eq(amount)
        );
    }

    @Test
    void onTransactionCreated_shouldNotThrowExceptionWhenWalletServiceSucceeds() {
        TransactionCreatedEvent event = mock(TransactionCreatedEvent.class);
        when(event.transactionId()).thenReturn(new TransactionId(UUID.randomUUID()));
        when(event.userId()).thenReturn(new UserId(UUID.randomUUID()));
        when(event.payerId()).thenReturn(new WalletId(UUID.randomUUID()));
        when(event.payeeId()).thenReturn(new WalletId(UUID.randomUUID()));
        when(event.amount()).thenReturn(BigDecimal.ONE);

        assertDoesNotThrow(() -> listener.onTransactionCreated(event));
    }
}
