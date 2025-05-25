package br.com.victorpizzaia.wallet_service_assignment.shared.domain;

import java.util.UUID;

import org.springframework.util.Assert;

public record TransactionId(UUID id) {
    public TransactionId {
        Assert.notNull(id, "Id must not be null");
    }

    public TransactionId() {
        this(UUID.randomUUID());
    }

    public TransactionId(String id) {
        this(UUID.fromString(id));
    }
}
