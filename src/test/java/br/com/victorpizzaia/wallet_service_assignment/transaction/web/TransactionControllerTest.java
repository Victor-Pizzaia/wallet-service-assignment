package br.com.victorpizzaia.wallet_service_assignment.transaction.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.CreateTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

class TransactionControllerTest {

    private TransactionUseCase transactionUseCase;
    private TransactionController transactionController;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        transactionUseCase = mock(TransactionUseCase.class);
        transactionController = new TransactionController(transactionUseCase);
        authentication = mock(Authentication.class);
    }

    @Test
    void createUserAndWallet_shouldCallUseCaseAndReturnOk() {
        String user = UUID.randomUUID().toString();
        when(authentication.getName()).thenReturn(user);
        CreateTransactionRequest request = new CreateTransactionRequest(
                new WalletId(UUID.randomUUID()),
                new WalletId(UUID.randomUUID()),
                new BigDecimal(100)
        );

        ResponseEntity<Void> response = transactionController.createUserAndWallet(authentication, request);

        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<CreateTransactionRequest> requestCaptor = ArgumentCaptor.forClass(CreateTransactionRequest.class);
        verify(transactionUseCase, times(1)).createTransaction(userIdCaptor.capture(), requestCaptor.capture());

        assertEquals(user, userIdCaptor.getValue().id().toString());
        assertEquals(request, requestCaptor.getValue());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNull(response.getBody());
    }
}
