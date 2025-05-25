package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;

@Component
public class TransactionCreatedEventListener {
    private final WalletService walletService;

    public TransactionCreatedEventListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @ApplicationModuleListener
    public void onTransactionCreated(TransactionCreatedEvent event) {
        walletService.transfer(
            event.transactionId(),
            event.userId(),
            event.payerId(),
            event.payeeId(),
            event.amount()
        );
    }
}
