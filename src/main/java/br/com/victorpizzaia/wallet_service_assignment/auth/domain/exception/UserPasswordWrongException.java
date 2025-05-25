package br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception;

import lombok.Getter;

@Getter
public class UserPasswordWrongException extends RuntimeException {
    private final String message;

    public UserPasswordWrongException(String message) {
        this.message = message;
    }
}
