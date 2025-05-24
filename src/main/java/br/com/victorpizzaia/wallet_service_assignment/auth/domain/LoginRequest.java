package br.com.victorpizzaia.wallet_service_assignment.auth.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull @NotBlank String identifier, @NotNull @NotBlank String password) {
}
