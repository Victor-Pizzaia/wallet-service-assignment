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

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void updateTransactionStatus(TransactionId transactionId, TransactionStatus status, String message) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow();
        switch (status) {
            case COMPLETED -> transaction.complete();
            case FAILED -> transaction.fail(message);
            default -> throw new IllegalArgumentException("Invalid status %s".formatted(status));
        }

        transactionRepository.save(transaction);
    }

	@Override
    @Transactional
	public void createTransaction(TransactionId transactionId, WalletId payerId, WalletId payeeKey, BigDecimal amount) {
		Transaction transaction = new Transaction(transactionId, payerId, payeeKey, amount);
        transactionRepository.save(transaction);
	}
}
