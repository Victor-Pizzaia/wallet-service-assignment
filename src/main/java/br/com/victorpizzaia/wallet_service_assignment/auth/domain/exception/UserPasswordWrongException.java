package br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UserPasswordWrongException extends RuntimeException {
    private final String message;

    public UserPasswordWrongException(String message) {
        log.error("User password is wrong: {}", message);
        this.message = message;
    }
}
