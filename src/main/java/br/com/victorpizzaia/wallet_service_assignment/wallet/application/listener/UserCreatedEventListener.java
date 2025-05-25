package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;

@Component
public class UserCreatedEventListener {

    private final WalletService walletService;

    public UserCreatedEventListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @ApplicationModuleListener
    public void onUserCreated(UserCreatedEvent event) {
        walletService.createWallet(event.userId());
    }
    
}
