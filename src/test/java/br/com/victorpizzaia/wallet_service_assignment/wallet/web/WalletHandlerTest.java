package br.com.victorpizzaia.wallet_service_assignment.wallet.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.UnauthorizeOperationException;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.exception.WalletNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class WalletHandlerTest {

    @Test
    void handleWalletNotFoundException_ReturnsNotFoundResponse() {
        String message = "Wallet not found";
        int code = 404;
        WalletNotFoundException exception = new WalletNotFoundException(message, code);

        WalletHandler handler = new WalletHandler();
        ResponseEntity<ErrorResponse> response = handler.handleWalletNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().message());
        assertEquals(code, response.getBody().code());
    }

    @Test
    void handleUnauthorizeOperationException_ReturnsUnauthorizedResponse() {
        String message = "Unauthorized operation";
        int code = 403;
        UnauthorizeOperationException exception = new UnauthorizeOperationException(message, code);

        WalletHandler handler = new WalletHandler();
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorizeOperationException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(message, response.getBody().message());
        assertEquals(code, response.getBody().code());
    }
}
