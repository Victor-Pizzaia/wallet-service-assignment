package br.com.victorpizzaia.wallet_service_assignment.wallet.domain;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;

public record TransactionCreatedResponse(TransactionId transactionId, String status, String message) {
    
}
