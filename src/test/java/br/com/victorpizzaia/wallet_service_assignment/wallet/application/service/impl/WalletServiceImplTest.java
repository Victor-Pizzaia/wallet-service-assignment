package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionUpdateStatusEvent;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.WalletUpdatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.UnauthorizeOperationException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    private WalletRepository walletRepository;
    private ApplicationEventPublisher eventPublisher;
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        walletService = new WalletServiceImpl(walletRepository, eventPublisher);
    }

    @Test
    void createWallet_shouldSaveNewWallet() {
        UserId userId = new UserId(UUID.randomUUID());
        Wallet wallet = new Wallet(userId);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.createWallet(userId);

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void getActualBalance_shouldReturnBalance_whenWalletExists() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal expectedBalance = new BigDecimal("100.00");

        when(walletRepository.findBalanceByUserId(userId)).thenReturn(Optional.of(expectedBalance));

        BigDecimal balance = walletService.getActualBalance(userId);

        assertEquals(expectedBalance, balance);
    }

    @Test
    void getActualBalance_shouldThrow_whenWalletNotFound() {
        UserId userId = new UserId(UUID.randomUUID());
        when(walletRepository.findBalanceByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getActualBalance(userId));
    }

    @Test
    void deposit_shouldDepositAndPublishEvent() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("50.00");
        Wallet wallet = mock(Wallet.class);
        WalletId walletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(wallet.getId()).thenReturn(walletId);
        when(wallet.getBalance()).thenReturn(new BigDecimal("150.00"));

        walletService.deposit(userId, amount);

        verify(wallet).deposit(amount);
        verify(walletRepository).save(wallet);

        ArgumentCaptor<WalletUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(WalletUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        WalletUpdatedEvent event = eventCaptor.getValue();
        assertEquals(userId, event.userId());
        assertEquals(walletId, event.walletId());
        assertEquals(amount, event.amount());
        assertEquals("DEPOSIT", event.walletTransactionType());
    }

    @Test
    void deposit_shouldThrow_whenWalletNotFound() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("10.00");
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.deposit(userId, amount));
    }

    @Test
    void withdraw_shouldWithdrawAndPublishEvent() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("20.00");
        Wallet wallet = mock(Wallet.class);
        WalletId walletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(wallet.getId()).thenReturn(walletId);
        when(wallet.getBalance()).thenReturn(new BigDecimal("80.00"));

        walletService.withdraw(userId, amount);

        verify(wallet).withdraw(amount);
        verify(walletRepository).save(wallet);

        ArgumentCaptor<WalletUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(WalletUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        WalletUpdatedEvent event = eventCaptor.getValue();
        assertEquals(userId, event.userId());
        assertEquals(walletId, event.walletId());
        assertEquals(amount, event.amount());
        assertEquals("WITHDRAW", event.walletTransactionType());
    }

    @Test
    void withdraw_shouldThrow_whenWalletNotFound() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("5.00");
        when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.withdraw(userId, amount));
    }

    @Test
    void transfer_shouldTransferAndPublishEvents() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("30.00");

        Wallet payerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);

        when(walletRepository.findById(payerId)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findById(payeeId)).thenReturn(Optional.of(payeeWallet));
        when(payerWallet.getUserId()).thenReturn(userId);
        when(payerWallet.getId()).thenReturn(payerId);
        when(payeeWallet.getUserId()).thenReturn(new UserId(UUID.randomUUID()));
        when(payeeWallet.getId()).thenReturn(payeeId);
        when(payerWallet.getBalance()).thenReturn(new BigDecimal("70.00"));
        when(payeeWallet.getBalance()).thenReturn(new BigDecimal("130.00"));

        walletService.transfer(transactionId, userId, payerId, payeeId, amount);

        verify(payerWallet).withdraw(amount);
        verify(payeeWallet).deposit(amount);
        verify(walletRepository).saveAll(List.of(payerWallet, payeeWallet));

        verify(eventPublisher, times(2)).publishEvent(isA(WalletUpdatedEvent.class));
        verify(eventPublisher).publishEvent(isA(TransactionUpdateStatusEvent.class));
    }

    @Test
    void transfer_shouldThrow_whenPayerWalletNotFound() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("40.00");

        when(walletRepository.findById(payerId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                walletService.transfer(transactionId, userId, payerId, payeeId, amount));
    }

    @Test
    void transfer_shouldThrow_whenPayeeWalletNotFound() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("25.00");

        Wallet payerWallet = mock(Wallet.class);
        when(walletRepository.findById(payerId)).thenReturn(Optional.of(payerWallet));
        when(payerWallet.getUserId()).thenReturn(userId);
        when(walletRepository.findById(payeeId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                walletService.transfer(transactionId, userId, payerId, payeeId, amount));
    }

    @Test
    void transfer_shouldThrow_whenUnauthorizedUser() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("60.00");

        Wallet payerWallet = mock(Wallet.class);
        when(walletRepository.findById(payerId)).thenReturn(Optional.of(payerWallet));
        when(payerWallet.getUserId()).thenReturn(new UserId(UUID.randomUUID()));

        assertThrows(UnauthorizeOperationException.class, () ->
                walletService.transfer(transactionId, userId, payerId, payeeId, amount));

        verify(eventPublisher).publishEvent(isA(TransactionUpdateStatusEvent.class));
    }

    @Test
    void transfer_shouldPublishFailedEvent_whenExceptionOccurs() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        WalletId payerId = new WalletId(UUID.randomUUID());
        WalletId payeeId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("70.00");

        Wallet payerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);

        when(walletRepository.findById(payerId)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findById(payeeId)).thenReturn(Optional.of(payeeWallet));
        when(payerWallet.getUserId()).thenReturn(userId);

        doThrow(new RuntimeException("withdraw error")).when(payerWallet).withdraw(amount);

        assertThrows(RuntimeException.class, () ->
                walletService.transfer(transactionId, userId, payerId, payeeId, amount));

        verify(eventPublisher).publishEvent(isA(TransactionUpdateStatusEvent.class));
    }
}