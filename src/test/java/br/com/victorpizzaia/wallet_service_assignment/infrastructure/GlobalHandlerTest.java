package br.com.victorpizzaia.wallet_service_assignment.infrastructure;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ErrorResponse;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalHandlerTest {

    private final GlobalHandler globalHandler = new GlobalHandler();

    @Test
    void handleIllegalArgumentException_returnsBadRequestWithErrorResponse() {
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<ErrorResponse> response = globalHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().message());
        assertEquals(400, response.getBody().code());
    }

    @Test
    void handleMethodArgumentNotValidException_returnsValidationErrorResponse() {
        FieldError fieldError1 = new FieldError("object", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("object", "field2", "must be positive");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ValidationErrorResponse> response = globalHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ValidationErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Field Error", body.message());
        List<Map.Entry<String, String>> errors = body.errors();
        assertEquals(2, errors.size());
        assertTrue(errors.contains(Map.entry("field1", "must not be null")));
        assertTrue(errors.contains(Map.entry("field2", "must be positive")));
    }
}
