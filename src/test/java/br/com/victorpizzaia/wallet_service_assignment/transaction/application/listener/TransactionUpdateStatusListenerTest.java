package br.com.victorpizzaia.wallet_service_assignment.transaction.application.listener;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.TransactionUpdateStatusEvent;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.service.TransactionService;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionUpdateStatusListenerTest {

    private TransactionService transactionService;
    private TransactionUpdateStatusListener listener;

    @BeforeEach
    void setUp() {
        transactionService = mock(TransactionService.class);
        listener = new TransactionUpdateStatusListener(transactionService);
    }

    @Test
    void onTransactionUpdated_shouldCallUpdateTransactionStatusWithCorrectArguments() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        String status = "COMPLETED";
        String message = "Transaction completed successfully";
        TransactionUpdateStatusEvent event = mock(TransactionUpdateStatusEvent.class);

        when(event.transactionId()).thenReturn(transactionId);
        when(event.status()).thenReturn(status);
        when(event.message()).thenReturn(message);

        listener.onTransactionUpdated(event);

        ArgumentCaptor<TransactionId> idCaptor = ArgumentCaptor.forClass(TransactionId.class);
        ArgumentCaptor<TransactionStatus> statusCaptor = ArgumentCaptor.forClass(TransactionStatus.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(transactionService, times(1)).updateTransactionStatus(
                idCaptor.capture(),
                statusCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals(transactionId, idCaptor.getValue());
        assertEquals(TransactionStatus.valueOf(status), statusCaptor.getValue());
        assertEquals(message, messageCaptor.getValue());
    }

    @Test
    void onTransactionUpdated_shouldThrowExceptionForInvalidStatus() {
        TransactionId transactionId = new TransactionId(UUID.randomUUID());
        String invalidStatus = "FAILED";
        String message = "Erro ocurrend on transaction";
        TransactionUpdateStatusEvent event = mock(TransactionUpdateStatusEvent.class);

        when(event.transactionId()).thenReturn(transactionId);
        when(event.status()).thenReturn(invalidStatus);
        when(event.message()).thenReturn(message);

        listener.onTransactionUpdated(event);

        ArgumentCaptor<TransactionId> idCaptor = ArgumentCaptor.forClass(TransactionId.class);
        ArgumentCaptor<TransactionStatus> statusCaptor = ArgumentCaptor.forClass(TransactionStatus.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(transactionService, times(1)).updateTransactionStatus(
                idCaptor.capture(),
                statusCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals(transactionId, idCaptor.getValue());
        assertEquals(TransactionStatus.valueOf(invalidStatus), statusCaptor.getValue());
        assertEquals(message, messageCaptor.getValue());
    }
}
