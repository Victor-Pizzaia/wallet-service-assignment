package br.com.victorpizzaia.wallet_service_assignment.shared.domain;

import java.util.UUID;

import org.springframework.util.Assert;

public record UserId(UUID id) {

    public UserId {
        Assert.notNull(id, "Id must not be null");
    }

    public UserId() {
        this(UUID.randomUUID());
    }
}
