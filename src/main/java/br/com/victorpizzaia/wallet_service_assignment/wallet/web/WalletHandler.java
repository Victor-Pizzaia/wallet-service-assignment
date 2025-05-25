package br.com.victorpizzaia.wallet_service_assignment.wallet.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.UnauthorizeOperationException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;

@RestControllerAdvice
public class WalletHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWalletNotFoundException(WalletNotFoundException exception) {
        ErrorResponse errorResponseDTO = new ErrorResponse(exception.getMessage(), exception.getCode());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizeOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizeOperationException(UnauthorizeOperationException exception) {
        ErrorResponse errorResponseDTO = new ErrorResponse(exception.getMessage(), exception.getCode());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }
}
