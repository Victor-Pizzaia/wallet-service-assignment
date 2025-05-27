package br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class UnauthorizeOperationException extends RuntimeException {

    private final String message;
    private final int code;

    public UnauthorizeOperationException(String message, int code) {
        log.error("Unauthorized operation: {}", message);
        this.message = message;
        this.code = code;
    }
}
