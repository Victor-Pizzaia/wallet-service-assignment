package br.com.victorpizzaia.wallet_service_assignment.user.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.exception.UserAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class UserHandlerTest {

    @Test
    void handleUserAlreadyExistException_shouldReturnBadRequestAndErrorResponse() {
        String message = "User already exists";
        int code = 409;
        UserAlreadyExistException exception = new UserAlreadyExistException(message, code);
        UserHandler handler = new UserHandler();

        ResponseEntity<ErrorResponse> response = handler.handleUserAlreadyExistException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().message());
        assertEquals(code, response.getBody().code());
    }
}
