package br.com.victorpizzaia.wallet_service_assignment.wallet_history.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.exception.InvalidDateFormatException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class WalletHistoryHandler {

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateFormatException(InvalidDateFormatException exception) {
        ErrorResponse errorResponseDTO = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
