package br.com.victorpizzaia.wallet_service_assignment.transaction.application.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.TransactionService;

@Component
public class TransactionCreatedEventListener {
    private final TransactionService transactionService;

    public TransactionCreatedEventListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApplicationModuleListener
    public void onTransactionCreated(TransactionCreatedEvent event) {
        transactionService.createTransaction(
            event.transactionId(),
            event.payerId(),
            event.payeeKey(),
            event.amount()
        );
    }
}
