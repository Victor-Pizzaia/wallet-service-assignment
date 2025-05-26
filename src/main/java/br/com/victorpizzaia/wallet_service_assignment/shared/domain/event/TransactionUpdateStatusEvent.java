package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.io.Serializable;

import org.springframework.modulith.events.Externalized;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;

@Externalized
public record TransactionUpdateStatusEvent(TransactionId transactionId, String status, String message) implements Serializable {
}
