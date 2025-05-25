package br.com.victorpizzaia.wallet_service_assignment.shared.domain;

import java.util.UUID;

import org.springframework.util.Assert;

public record WalletHistoryId(UUID id) {

    public WalletHistoryId {
        Assert.notNull(id, "Id must not be null");
    }

    public WalletHistoryId() {
        this(UUID.randomUUID());
    }

    public WalletHistoryId(String id) {
        this(UUID.fromString(id));
    }
}
