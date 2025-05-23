package br.com.victorpizzaia.wallet_service_assignment.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
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
