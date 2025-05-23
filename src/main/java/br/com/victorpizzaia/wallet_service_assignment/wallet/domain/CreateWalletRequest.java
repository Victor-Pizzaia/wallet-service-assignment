package br.com.victorpizzaia.wallet_service_assignment.wallet.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
    @NotNull
    @NotBlank
    String fullname,
    @NotNull
    @NotBlank
    String cpf,
    @NotNull
    @NotBlank
    String email,
    @NotNull
    @NotBlank
    String plainPassword
) {
    
}
