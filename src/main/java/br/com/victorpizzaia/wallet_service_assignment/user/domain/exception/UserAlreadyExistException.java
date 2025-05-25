package br.com.victorpizzaia.wallet_service_assignment.user.domain.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistException extends RuntimeException {
    private final String message;
    private final int code;

    public UserAlreadyExistException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
