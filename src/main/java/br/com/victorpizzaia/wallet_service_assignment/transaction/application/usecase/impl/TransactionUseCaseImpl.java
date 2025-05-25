package br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase.impl;

import org.springframework.context.ApplicationEventPublisher;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.CreateTransactionRequest;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.Transaction;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.TransactionRepository;
import jakarta.transaction.Transactional;

@UseCase
public class TransactionUseCaseImpl implements TransactionUseCase {

    private final TransactionRepository transactionRepositoy;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionUseCaseImpl(TransactionRepository transactionRepositoy, ApplicationEventPublisher eventPublisher) {
        this.transactionRepositoy = transactionRepositoy;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public TransactionId createTransaction(UserId userId, CreateTransactionRequest request) {
        Transaction transaction = new Transaction(
            new TransactionId(),
            request.payerId(),
            request.payeeId(),
            request.amount()
        );

        transactionRepositoy.save(transaction);

        eventPublisher.publishEvent(new TransactionCreatedEvent(transaction.getId(), userId, request.payerId(), request.payeeId(), request.amount()));

        return transaction.getId();
    }
}
