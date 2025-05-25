package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.WalletUpdatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;

@Component
public class WalletEventListener {

    private final WalletHistoryService walletHistoryService;

    public WalletEventListener(WalletHistoryService walletHistoryService) {
        this.walletHistoryService = walletHistoryService;
    }

    @ApplicationModuleListener
    public void onWalletUpdated(WalletUpdatedEvent event) {
        walletHistoryService.recordWalletHistory(event.userId(), event.walletId(), event.balance(), event.amount(), event.walletTransactionType());
    }
}
