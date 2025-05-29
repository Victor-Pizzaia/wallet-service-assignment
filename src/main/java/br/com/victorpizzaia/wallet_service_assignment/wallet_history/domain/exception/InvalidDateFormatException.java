package br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class InvalidDateFormatException extends RuntimeException {
    private final String message;

    public InvalidDateFormatException(String message) {
        log.error(message);
        this.message = message;
    }
}
