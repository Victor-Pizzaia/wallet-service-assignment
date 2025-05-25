package br.com.victorpizzaia.wallet_service_assignment.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserNotFound;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserPasswordWrongException;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;

@RestControllerAdvice
public class AuthHandler {
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFound exception) {
        ErrorResponse errorResponseDTO = new ErrorResponse(exception.getMessage(), 404);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserPasswordWrongException.class)
    public ResponseEntity<ErrorResponse> handleUserPasswordWronException(UserPasswordWrongException exception) {
        ErrorResponse errorResponseDTO = new ErrorResponse(exception.getMessage(), 400);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
