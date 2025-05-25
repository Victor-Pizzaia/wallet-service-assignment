package br.com.victorpizzaia.wallet_service_assignment.transaction.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionUpdateStatusEvent;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.TransactionService;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;

@Component
public class TransactionUpdateStatusListener {
    private final TransactionService transactionService;

    public TransactionUpdateStatusListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApplicationModuleListener
    public void onTransactionUpdated(TransactionUpdateStatusEvent event) {
        transactionService.updateTransactionStatus(
            event.transactionId(),
            TransactionStatus.valueOf(event.status()),
            event.message()
        );
    }
}
