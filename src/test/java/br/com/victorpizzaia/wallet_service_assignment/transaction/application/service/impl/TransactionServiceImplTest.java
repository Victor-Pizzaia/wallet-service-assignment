package br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.Transaction;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private TransactionRepository transactionRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionServiceImpl(transactionRepository);
    }

    @Test
    void updateTransactionStatus_shouldCompleteTransaction_whenStatusIsCompleted() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        Transaction transaction = new Transaction(transactionId, new WalletId(UUID.randomUUID()), new WalletId(UUID.randomUUID()), new BigDecimal("100.00"));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.updateTransactionStatus(transactionId, TransactionStatus.COMPLETED, null);

        assertEquals(transaction.getStatus(), TransactionStatus.COMPLETED);
    }

    @Test
    void updateTransactionStatus_shouldFailTransaction_whenStatusIsFailed() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        Transaction transaction = new Transaction(transactionId, new WalletId(UUID.randomUUID()), new WalletId(UUID.randomUUID()), new BigDecimal("100.00"));
        String failMessage = "Insufficient funds";

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.updateTransactionStatus(transactionId, TransactionStatus.FAILED, failMessage);

        assertEquals(transaction.getStatus(), TransactionStatus.FAILED);
    }

    @Test
    void updateTransactionStatus_shouldThrowException_whenTransactionNotFound() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () ->
            transactionService.updateTransactionStatus(transactionId, TransactionStatus.COMPLETED, null)
        );
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void updateTransactionStatus_shouldThrowException_whenStatusIsInvalid() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        Transaction transaction = new Transaction(transactionId, new WalletId(UUID.randomUUID()), new WalletId(UUID.randomUUID()), new BigDecimal("100.00"));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionStatus invalidStatus = mock(TransactionStatus.class);
        when(invalidStatus.name()).thenReturn("UNKNOWN");

        assertThrows(IllegalArgumentException.class, () ->
            transactionService.updateTransactionStatus(transactionId, invalidStatus, null)
        );
        verify(transactionRepository, never()).save(any());
    }
}
