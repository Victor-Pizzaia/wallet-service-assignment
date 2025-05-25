package br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.CreateTransactionRequest;

public interface TransactionUseCase {
    TransactionId createTransaction(UserId userId, CreateTransactionRequest request);
}
