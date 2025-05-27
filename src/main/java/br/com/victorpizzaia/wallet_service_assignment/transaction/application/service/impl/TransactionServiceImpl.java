package br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.TransactionService;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.Transaction;
import br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence.TransactionRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void updateTransactionStatus(TransactionId transactionId, TransactionStatus status, String message) {
        log.info("Updating transaction status for transactionId: {}, status: {}, message: {}", transactionId, status, message);
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow();
        switch (status) {
            case COMPLETED -> transaction.complete();
            case FAILED -> transaction.fail(message);
            default -> throw new IllegalArgumentException("Invalid status %s".formatted(status));
        }

        log.info("Transaction status updated for transactionId: {}, new status: {}", transactionId, transaction.getStatus());
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void createTransaction(TransactionId transactionId, WalletId payerId, WalletId payeeKey, BigDecimal amount) {
        log.info("Creating transaction with transactionId: {}, payerId: {}, payeeKey: {}, amount: {}", transactionId, payerId, payeeKey, amount);
        Transaction transaction = new Transaction(transactionId, payerId, payeeKey, amount);
        transactionRepository.save(transaction);
    }
}
