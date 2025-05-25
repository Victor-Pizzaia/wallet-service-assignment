package br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionUpdateStatusEvent;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.service.WalletService;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.Wallet;
import br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher statusUpdater;
    private static final String FAILED_STATUS = "FAILED";
    private static final String COMPLETED_STATUS = "COMPLETED";

    public WalletServiceImpl(WalletRepository walletRepository, ApplicationEventPublisher statusUpdater) {
        this.walletRepository = walletRepository;
        this.statusUpdater = statusUpdater;
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
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + userId));
    }

    @Override
    @Transactional
    public void deposit(UserId userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + userId));

        wallet.deposit(amount);
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void withdraw(UserId userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user: " + userId));

        wallet.withdraw(amount);
        walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void transfer(TransactionId transactionId, UserId userId, WalletId payerId, WalletId payeeId, BigDecimal amount) {
        Wallet payerWallet = walletRepository.findById(payerId).orElseThrow(() -> {
            publishTransactionEvent(transactionId, FAILED_STATUS, "Payer wallet not found");
            return new RuntimeException("Payer wallet not found: " + payerId);
        });

        if (!payerWallet.getUserId().equals(userId)) {
            publishTransactionEvent(transactionId, FAILED_STATUS, "Unauthorized transfer attempt");
            throw new RuntimeException("Unauthorized transfer attempt by user: " + userId);
        }

        Wallet payeeWallet = walletRepository.findById(payeeId).orElseThrow(() -> {
            publishTransactionEvent(transactionId, FAILED_STATUS, "Payer wallet not found");
            return new RuntimeException("Payee wallet not found: " + payerId);
        });

        try {
            payerWallet.withdraw(amount);
            payeeWallet.deposit(amount);

            walletRepository.saveAll(List.of(payerWallet, payeeWallet));

            publishTransactionEvent(transactionId, COMPLETED_STATUS, "Transfer successful");
        } catch (Exception e) {
            publishTransactionEvent(transactionId, FAILED_STATUS, "Unexpected error");
        }
    }

    private void publishTransactionEvent(TransactionId transactionId, String status, String message) {
        statusUpdater.publishEvent(new TransactionUpdateStatusEvent(transactionId, status, message));
    }
}
