package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.UnauthorizeOperationException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final String FAILED_STATUS = "FAILED";
    private static final String COMPLETED_STATUS = "COMPLETED";
    private static final String DEPOSIT = "DEPOSIT";
    private static final String WITHDRAW = "WITHDRAW";

    public WalletServiceImpl(WalletRepository walletRepository, ApplicationEventPublisher eventPublisher) {
        this.walletRepository = walletRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void createWallet(UserId userId) {
        Wallet newWallet = new Wallet(userId);
        walletRepository.save(newWallet);
    }

    @Override
    public BigDecimal getActualBalance(UserId userId) {
        return walletRepository.findBalanceByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));
    }

    @Override
    @Transactional
    public void deposit(UserId userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));

        wallet.deposit(amount);
        walletRepository.save(wallet);
        publishWalletUpdateEvent(userId, wallet.getId(), wallet.getBalance(), amount, DEPOSIT);
    }

    @Override
    @Transactional
    public void withdraw(UserId userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user: " + userId, 404));

        wallet.withdraw(amount);
        walletRepository.save(wallet);
        publishWalletUpdateEvent(userId, wallet.getId(), wallet.getBalance(), amount, WITHDRAW);
    }

    @Override
    @Transactional
    public void transaction(UserId payerId, String payeeKey, BigDecimal amount) {
        Wallet payerWallet = validateWalletExists(walletRepository.findByUserId(payerId));
        Wallet payeeWallet = validateWalletExists(walletRepository.findByUserKey(payeeKey));

        TransactionId transactionId = new TransactionId();
        publishCreateTransactionEvent(transactionId, payerWallet.getId(), payeeWallet.getId(), amount);

        validatePayerAndPayeeAsTheSame(payerWallet, payeeWallet, transactionId);
        executeTransaction(transactionId, payerWallet, payeeWallet, amount);
    }

    private Wallet validateWalletExists(Optional<Wallet> wallet) {
        return wallet.orElseThrow(() -> new WalletNotFoundException("Wallet not found", 404));
    }

    private void validatePayerAndPayeeAsTheSame(Wallet payerWallet, Wallet payeeWallet, TransactionId transactionId) {
        if (payerWallet.getId().equals(payeeWallet.getId())) {
            publishUpdateTransactionEvent(transactionId, FAILED_STATUS, "Payer and payee cannot be the same wallet");
            throw new UnauthorizeOperationException("Payer and payee cannot be the same wallet for transaction", 403);
        }
    }

    @Transactional
    public void executeTransaction(TransactionId transactionId, Wallet payerWallet, Wallet payeeWallet, BigDecimal amount) {
        payerWallet.withdraw(amount);
        payeeWallet.deposit(amount);

        walletRepository.saveAll(List.of(payerWallet, payeeWallet));

        publishWalletUpdateEvent(payerWallet.getUserId(), payerWallet.getId(), payerWallet.getBalance(), amount, WITHDRAW);
        publishWalletUpdateEvent(payeeWallet.getUserId(), payeeWallet.getId(), payeeWallet.getBalance(), amount, DEPOSIT);
        publishUpdateTransactionEvent(transactionId, COMPLETED_STATUS, "Transfer successful");
    }

    private void publishCreateTransactionEvent(TransactionId transactionId, WalletId payerId, WalletId payeeId, BigDecimal amount) {
        eventPublisher.publishEvent(new TransactionCreatedEvent(transactionId, payerId, payeeId, amount));
    }

    private void publishUpdateTransactionEvent(TransactionId transactionId, String status, String message) {
        eventPublisher.publishEvent(new TransactionUpdateStatusEvent(transactionId, status, message));
    }

    private void publishWalletUpdateEvent(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType) {
        eventPublisher.publishEvent(new WalletUpdatedEvent(userId, walletId, balance, amount, transactionType));
    }
}
