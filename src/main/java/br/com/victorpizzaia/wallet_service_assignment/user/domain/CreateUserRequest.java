package br.com.victorpizzaia.wallet_service_assignment.user.domain;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Name must contain only letters and spaces")
    String fullname,
    @NotNull
    @CPF
    String cpf,
    @NotNull
    @NotBlank
    @Email(message = "Invalid email format")
    String email,
    @NotNull
    @NotBlank
    String plainPassword
) {

}
