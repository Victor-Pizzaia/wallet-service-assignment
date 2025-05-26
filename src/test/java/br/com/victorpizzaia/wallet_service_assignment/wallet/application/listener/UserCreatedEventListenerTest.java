package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.UUID;

class UserCreatedEventListenerTest {

    private WalletService walletService;
    private UserCreatedEventListener listener;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        listener = new UserCreatedEventListener(walletService);
    }

    @Test
    void onUserCreated_shouldCallCreateWalletWithCorrectUserId() {
        UserId userId = new UserId(UUID.randomUUID());
        UserCreatedEvent event = mock(UserCreatedEvent.class);
        when(event.userId()).thenReturn(userId);

        listener.onUserCreated(event);

        verify(walletService, times(1)).createWallet(userId);
    }
}
