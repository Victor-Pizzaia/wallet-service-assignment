package br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
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
}
