package br.com.victorpizzaia.wallet_service_assignment.transaction.application.service;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;

public interface TransactionService {
    void updateTransactionStatus(TransactionId transactionId, TransactionStatus status, String message);
}
