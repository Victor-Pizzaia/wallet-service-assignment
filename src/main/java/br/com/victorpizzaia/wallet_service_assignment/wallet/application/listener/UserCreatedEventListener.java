package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;
import jakarta.transaction.Transactional;

@Component
public class UserCreatedEventListener {

    private final WalletRepository walletRepository;

    public UserCreatedEventListener(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    @ApplicationModuleListener
    public void onUserCreated(UserCreatedEvent event) {
        Wallet newWallet = new Wallet(event.userId());
        walletRepository.save(newWallet);
    }
    
}
