package br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception;

import lombok.Getter;

@Getter
public class WalletNotFoundException extends RuntimeException {

    private final String message;
    private final int code;

    public WalletNotFoundException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
