package br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UserNotFound extends RuntimeException {
    private final String message;

    public UserNotFound(String message) {
        log.error("User not found: {}", message);
        this.message = message;
    }
}
