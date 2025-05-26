package br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.CreateTransactionRequest;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.Transaction;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionUseCaseImplTest {

    private TransactionRepository transactionRepository;
    private ApplicationEventPublisher eventPublisher;
    private TransactionUseCaseImpl transactionUseCase;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        transactionUseCase = new TransactionUseCaseImpl(transactionRepository, eventPublisher);
    }

    @Test
    void createTransaction_shouldSaveTransactionAndPublishEventAndReturnId() {
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("100.00");
        CreateTransactionRequest request = mock(CreateTransactionRequest.class);
        when(request.payerId()).thenReturn(payerId);
        when(request.payeeId()).thenReturn(payeeId);
        when(request.amount()).thenReturn(amount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        ArgumentCaptor<TransactionCreatedEvent> eventCaptor = ArgumentCaptor.forClass(TransactionCreatedEvent.class);

        TransactionId returnedId = transactionUseCase.createTransaction(userId, request);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertNotNull(savedTransaction);
        assertNotNull(savedTransaction.getId());
        assertEquals(payerId, savedTransaction.getPayerId());
        assertEquals(payeeId, savedTransaction.getPayeeId());
        assertEquals(amount, savedTransaction.getAmount());

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        TransactionCreatedEvent publishedEvent = eventCaptor.getValue();
        assertNotNull(publishedEvent);
        assertEquals(savedTransaction.getId(), publishedEvent.transactionId());
        assertEquals(userId, publishedEvent.userId());
        assertEquals(payerId, publishedEvent.payerId());
        assertEquals(payeeId, publishedEvent.payeeId());
        assertEquals(amount, publishedEvent.amount());

        assertEquals(savedTransaction.getId(), returnedId);
    }
}
