package br.com.victorpizzaia.wallet_service_assignment.wallet_history.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.exception.InvalidDateFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class WalletHistoryHandlerTest {

    @Test
    void handleInvalidDateFormatException_ReturnsBadRequestAndErrorResponse() {
        WalletHistoryHandler handler = new WalletHistoryHandler();
        String errorMessage = "Invalid date format";
        InvalidDateFormatException exception = new InvalidDateFormatException(errorMessage);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidDateFormatException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().code());
    }
}