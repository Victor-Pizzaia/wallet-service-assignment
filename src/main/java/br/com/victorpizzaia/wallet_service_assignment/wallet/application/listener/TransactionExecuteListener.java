package br.com.victorpizzaia.wallet_service_assignment.wallet.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.ExecuteTransactionEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransactionExecuteListener {

    private final WalletService walletService;

    public TransactionExecuteListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @ApplicationModuleListener
    public void onTransactionCreatedExecute(ExecuteTransactionEvent event) {
        log.info("Received ExecuteTransactionEvent: {}", event.transactionId());
        walletService.executeTransaction(event.transactionId(), event.payerId(), event.payeeId(), event.amount());
    }
}
