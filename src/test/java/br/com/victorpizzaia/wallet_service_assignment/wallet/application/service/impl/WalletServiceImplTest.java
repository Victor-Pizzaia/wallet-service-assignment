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
    void transaction_shouldTransferFundsAndPublishEvents() {
        UserId payerId = new UserId(UUID.randomUUID());
        UserId payeeUserId = new UserId(UUID.randomUUID());
        String payeeKey = "payee-key";
        BigDecimal amount = new BigDecimal("30.00");

        Wallet payerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);
        WalletId payerWalletId = new WalletId(UUID.randomUUID());
        WalletId payeeWalletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(payerId)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserKey(payeeKey)).thenReturn(Optional.of(payeeWallet));
        when(payerWallet.getId()).thenReturn(payerWalletId);
        when(payeeWallet.getId()).thenReturn(payeeWalletId);
        when(payerWallet.getUserId()).thenReturn(payerId);
        when(payeeWallet.getUserId()).thenReturn(payeeUserId);
        when(payerWallet.getBalance()).thenReturn(new BigDecimal("70.00"));
        when(payeeWallet.getBalance()).thenReturn(new BigDecimal("130.00"));

        walletService.transaction(payerId, payeeKey, amount);

        verify(payerWallet).withdraw(amount);
        verify(payeeWallet).deposit(amount);
        verify(walletRepository).saveAll(List.of(payerWallet, payeeWallet));
    }

    @Test
    void transaction_shouldThrow_whenPayerWalletNotFound() {
        UserId payerId = new UserId(UUID.randomUUID());
        String payeeKey = "payee-key";
        BigDecimal amount = new BigDecimal("10.00");

        when(walletRepository.findByUserId(payerId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.transaction(payerId, payeeKey, amount));
    }

    @Test
    void transaction_shouldThrow_whenPayeeWalletNotFound() {
        UserId payerId = new UserId(UUID.randomUUID());
        String payeeKey = "payee-key";
        BigDecimal amount = new BigDecimal("10.00");
        Wallet payerWallet = mock(Wallet.class);

        when(walletRepository.findByUserId(payerId)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserKey(payeeKey)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.transaction(payerId, payeeKey, amount));
    }

    @Test
    void transaction_shouldThrow_whenPayerAndPayeeAreTheSame() {
        UserId payerId = new UserId(UUID.randomUUID());
        String payeeKey = "payee-key";
        BigDecimal amount = new BigDecimal("10.00");
        Wallet wallet = mock(Wallet.class);
        WalletId walletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(payerId)).thenReturn(Optional.of(wallet));
        when(walletRepository.findByUserKey(payeeKey)).thenReturn(Optional.of(wallet));
        when(wallet.getId()).thenReturn(walletId);

        assertThrows(UnauthorizeOperationException.class, () -> walletService.transaction(payerId, payeeKey, amount));
        verify(eventPublisher, atLeastOnce()).publishEvent(any(TransactionUpdateStatusEvent.class));
    }

    @Test
    void executeTransaction_shouldWithdrawFromPayerAndDepositToPayeeAndPublishEvents() {
        TransactionId transactionId = new TransactionId();
        UserId payerId = new UserId(UUID.randomUUID());
        UserId payeeId = new UserId(UUID.randomUUID());
        Wallet payerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);
        WalletId payerWalletId = new WalletId(UUID.randomUUID());
        WalletId payeeWalletId = new WalletId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("25.00");

        when(payerWallet.getUserId()).thenReturn(payerId);
        when(payeeWallet.getUserId()).thenReturn(payeeId);
        when(payerWallet.getId()).thenReturn(payerWalletId);
        when(payeeWallet.getId()).thenReturn(payeeWalletId);
        when(payerWallet.getBalance()).thenReturn(new BigDecimal("75.00"));
        when(payeeWallet.getBalance()).thenReturn(new BigDecimal("125.00"));

        walletService.executeTransaction(transactionId, payerWallet, payeeWallet, amount);

        verify(payerWallet).withdraw(amount);
        verify(payeeWallet).deposit(amount);
        verify(walletRepository).saveAll(List.of(payerWallet, payeeWallet));
        verify(eventPublisher, atLeastOnce()).publishEvent(any(WalletUpdatedEvent.class));
        verify(eventPublisher, atLeastOnce()).publishEvent(any(TransactionUpdateStatusEvent.class));
    }
}
