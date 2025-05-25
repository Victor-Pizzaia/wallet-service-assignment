package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;

public record TransactionUpdateStatusEvent(TransactionId transactionId, String status, String message) {
}
