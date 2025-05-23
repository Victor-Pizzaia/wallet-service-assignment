package br.com.victorpizzaia.wallet_service_assignment.shared.domain;

import java.util.UUID;

import org.springframework.util.Assert;

public record WalletId(UUID id) {

    public WalletId {
        Assert.notNull(id, "Id must not be null");
    }

    public WalletId() {
        this(UUID.randomUUID());
    }
}
