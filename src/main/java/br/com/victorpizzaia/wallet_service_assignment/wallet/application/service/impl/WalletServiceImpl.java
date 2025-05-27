package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionUpdateStatusEvent;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.WalletUpdatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.TransactionCreatedResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.UnauthorizeOperationException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;

    private static final String COMPLETED_STATUS = "COMPLETED";
    private static final String PENDING_STATUS = "PENDING";
    private static final String FAILED_STATUS = "FAILED";
    private static final String DEPOSIT = "DEPOSIT";
    private static final String WITHDRAW = "WITHDRAW";
    private static final String TRANSFER = "TRANSFER";
    private static final String CACHE_VALUE = "walletBalance";
    private static final String CACHE_KEY = "#userId";

    public WalletServiceImpl(WalletRepository walletRepository, ApplicationEventPublisher eventPublisher, CacheManager cacheManager) {
        this.walletRepository = walletRepository;
        this.eventPublisher = eventPublisher;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public void createWallet(UserId userId) {
        Wallet newWallet = new Wallet(userId);
        walletRepository.save(newWallet);
        log.info("Wallet created for user: {}, and id: {}", userId, newWallet.getId());
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = CACHE_KEY)
    public BigDecimal getActualBalance(UserId userId) {
        log.info("Retrieving balance for user: {}", userId);
        return walletRepository.findBalanceByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));
    }

    @Override
    @Transactional
    @CachePut(value = CACHE_VALUE, key = CACHE_KEY)
    public BigDecimal deposit(UserId userId, BigDecimal amount) {
        log.info("Depositing amount: {} for user: {}", amount, userId);
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));

        wallet.deposit(amount);
        walletRepository.save(wallet);
        publishWalletUpdateEvent(userId, wallet.getId(), wallet.getBalance(), amount, DEPOSIT);
        return wallet.getBalance();
    }

    @Override
    @Transactional
    @CachePut(value = CACHE_VALUE, key = CACHE_KEY)
    public BigDecimal withdraw(UserId userId, BigDecimal amount) {
        log.info("Withdrawing amount: {} for user: {}", amount, userId);
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));

        wallet.withdraw(amount);
        walletRepository.save(wallet);
        publishWalletUpdateEvent(userId, wallet.getId(), wallet.getBalance(), amount, WITHDRAW);
        return wallet.getBalance();
    }

    @Override
    @Transactional
    public TransactionCreatedResponse transaction(UserId payerId, String payeeKey, BigDecimal amount) {
        log.info("Processing transaction from payer: {} to payee: {} with amount: {}", payerId, payeeKey, amount);
        Wallet payerWallet = validateWalletExists(walletRepository.findByUserId(payerId));
        Wallet payeeWallet = validateWalletExists(walletRepository.findByUserKey(payeeKey));
        log.info("Payer wallet: {}, Payee wallet: {}", payerWallet.getId(), payeeWallet.getId());

        validatePayerAndPayeeAsTheSame(payerWallet, payeeWallet);

        TransactionId transactionId = new TransactionId();
        publishCreateTransactionEvent(transactionId, payerWallet.getId(), payeeWallet.getId(), amount);
        return new TransactionCreatedResponse(transactionId, PENDING_STATUS, "Transaction request accepted and being processed.");
    }

    private Wallet validateWalletExists(Optional<Wallet> wallet) {
        return wallet.orElseThrow(() -> new WalletNotFoundException("Wallet not found", 404));
    }

    private void validatePayerAndPayeeAsTheSame(Wallet payerWallet, Wallet payeeWallet) {
        if (payerWallet.getId().equals(payeeWallet.getId())) {
            throw new UnauthorizeOperationException("Payer and payee cannot be the same wallet for transaction", 403);
        }
    }

    @Override
    @Transactional
    public void executeTransaction(TransactionId transactionId, WalletId payerId, WalletId payeeId, BigDecimal amount) {
        log.info("Executing transaction ID: {} from payer: {} to payee: {} with amount: {}", transactionId, payerId, payeeId, amount);
        try {
            Wallet payerWallet = walletRepository.findById(payerId).orElseThrow(() -> new WalletNotFoundException("Wallet not found", 404));
            Wallet payeeWallet = walletRepository.findById(payeeId).orElseThrow(() -> new WalletNotFoundException("Wallet not found", 404));

            payerWallet.withdraw(amount);
            payeeWallet.deposit(amount);

            walletRepository.saveAll(List.of(payerWallet, payeeWallet));

            Cache cache = cacheManager.getCache(CACHE_VALUE);
            if (cache != null) {
                cache.put(payerWallet.getUserId(), payerWallet.getBalance());
                cache.put(payeeWallet.getUserId(), payeeWallet.getBalance());
            }

            log.info("Transaction: {} completed successfully", transactionId);
            publishWalletUpdateEvent(payerWallet.getUserId(), payerWallet.getId(), payerWallet.getBalance(), amount, TRANSFER);
            publishWalletUpdateEvent(payeeWallet.getUserId(), payeeWallet.getId(), payeeWallet.getBalance(), amount, TRANSFER);
            publishUpdateTransactionEvent(transactionId, COMPLETED_STATUS, "Transfer successful");
        } catch (Exception e) {
            publishUpdateTransactionEvent(transactionId, FAILED_STATUS, e.getMessage());
            throw e;
        }
    }

    private void publishCreateTransactionEvent(TransactionId transactionId, WalletId payerId, WalletId payeeId, BigDecimal amount) {
        log.info("Publishing TransactionCreatedEvent for transaction ID: {}", transactionId);
        eventPublisher.publishEvent(new TransactionCreatedEvent(transactionId, payerId, payeeId, amount));
    }

    private void publishUpdateTransactionEvent(TransactionId transactionId, String status, String message) {
        log.info("Publishing TransactionUpdateStatusEvent for transaction ID: {}, status: {}", transactionId, status);
        eventPublisher.publishEvent(new TransactionUpdateStatusEvent(transactionId, status, message));
    }

    private void publishWalletUpdateEvent(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType) {
        log.info("Publishing WalletUpdatedEvent for wallet: {}", walletId);
        eventPublisher.publishEvent(new WalletUpdatedEvent(userId, walletId, balance, amount, transactionType));
    }
}
