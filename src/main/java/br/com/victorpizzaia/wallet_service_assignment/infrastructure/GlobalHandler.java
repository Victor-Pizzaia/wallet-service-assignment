package br.com.victorpizzaia.wallet_service_assignment.infrastructure;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<Map.Entry<String, String>> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(error -> Map.entry(error.getField(), error.getDefaultMessage()))
            .toList();

        ValidationErrorResponse response = new ValidationErrorResponse("Field Error", errors);
        log.error("Validation error occurred: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), 400);
        log.error("Illegal argument error occurred: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
