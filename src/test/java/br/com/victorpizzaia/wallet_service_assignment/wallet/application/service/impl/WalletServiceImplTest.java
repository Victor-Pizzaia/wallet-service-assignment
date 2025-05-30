package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.WalletUpdatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    private WalletRepository walletRepository;
    private ApplicationEventPublisher eventPublisher;
    private WalletServiceImpl walletService;
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        walletService = new WalletServiceImpl(walletRepository, eventPublisher, cacheManager);
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
    void executeTransaction_shouldUpdateCacheWithNewBalances() {
        TransactionId transactionId = new TransactionId();
        WalletId payerWalletId = new WalletId(UUID.randomUUID());
        WalletId payeeWalletId = new WalletId(UUID.randomUUID());
        UserId payerUserId = new UserId(UUID.randomUUID());
        UserId payeeUserId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("40.00");

        Wallet payerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);

        when(walletRepository.findById(payerWalletId)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findById(payeeWalletId)).thenReturn(Optional.of(payeeWallet));
        when(payerWallet.getUserId()).thenReturn(payerUserId);
        when(payeeWallet.getUserId()).thenReturn(payeeUserId);
        when(payerWallet.getId()).thenReturn(payerWalletId);
        when(payeeWallet.getId()).thenReturn(payeeWalletId);
        when(payerWallet.getBalance()).thenReturn(new BigDecimal("60.00"));
        when(payeeWallet.getBalance()).thenReturn(new BigDecimal("140.00"));

        CacheManager cacheManagerMock = mock(CacheManager.class);
        Cache cacheMock = mock(Cache.class);
        when(cacheManagerMock.getCache("walletBalance")).thenReturn(cacheMock);

        walletService = new WalletServiceImpl(walletRepository, eventPublisher, cacheManagerMock);

        walletService.executeTransaction(transactionId, payerWalletId, payeeWalletId, amount);

        verify(cacheMock).put(payerUserId, new BigDecimal("60.00"));
        verify(cacheMock).put(payeeUserId, new BigDecimal("140.00"));
    }

    @Test
    void getActualBalance_shouldCacheResult() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal expectedBalance = new BigDecimal("200.00");
        when(walletRepository.findBalanceByUserId(userId)).thenReturn(Optional.of(expectedBalance));

        BigDecimal balance1 = walletService.getActualBalance(userId);
        BigDecimal balance2 = walletService.getActualBalance(userId);

        assertEquals(expectedBalance, balance1);
        assertEquals(expectedBalance, balance2);
        verify(walletRepository, times(2)).findBalanceByUserId(userId);
    }

    @Test
    void deposit_shouldUpdateCache() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("100.00");
        Wallet wallet = mock(Wallet.class);
        WalletId walletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(wallet.getId()).thenReturn(walletId);
        when(wallet.getBalance()).thenReturn(new BigDecimal("300.00"));

        CacheManager cacheManagerMock = mock(CacheManager.class);
        Cache cacheMock = mock(Cache.class);
        when(cacheManagerMock.getCache("walletBalance")).thenReturn(cacheMock);

        walletService = new WalletServiceImpl(walletRepository, eventPublisher, cacheManagerMock);

        walletService.deposit(userId, amount);
    }

    @Test
    void withdraw_shouldUpdateCache() {
        UserId userId = new UserId(UUID.randomUUID());
        BigDecimal amount = new BigDecimal("50.00");
        Wallet wallet = mock(Wallet.class);
        WalletId walletId = new WalletId(UUID.randomUUID());

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(wallet.getId()).thenReturn(walletId);
        when(wallet.getBalance()).thenReturn(new BigDecimal("250.00"));

        CacheManager cacheManagerMock = mock(CacheManager.class);
        Cache cacheMock = mock(Cache.class);
        when(cacheManagerMock.getCache("walletBalance")).thenReturn(cacheMock);

        walletService = new WalletServiceImpl(walletRepository, eventPublisher, cacheManagerMock);

        walletService.withdraw(userId, amount);
    }
}
