package br.com.victorpizzaia.wallet_service_assignment.auth.web;

import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserNotFound;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserPasswordWrongException;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class AuthHandlerTest {

    @Test
    void handleUserNotFoundException_returnsNotFoundResponse() {
        AuthHandler handler = new AuthHandler();
        String errorMessage = "User not found";
        UserNotFound exception = new UserNotFound(errorMessage);

        ResponseEntity<ErrorResponse> response = handler.handleUserNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(404, response.getBody().code());
    }

    @Test
    void handleUserPasswordWrongException_returnsBadRequestResponse() {
        AuthHandler handler = new AuthHandler();
        String errorMessage = "Password is wrong";
        UserPasswordWrongException exception = new UserPasswordWrongException(errorMessage);

        ResponseEntity<ErrorResponse> response = handler.handleUserPasswordWronException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(400, response.getBody().code());
    }
}
