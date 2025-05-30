package br.com.victorpizzaia.wallet_service_assignment.wallet.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.DepositUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.GetActualBalanceUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.WithdrawUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.TransactionCreatedResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletAmountRequest;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletTransactionRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

class WalletControllerTest {

    private GetActualBalanceUseCase getActualBalanceUseCase;
    private DepositUseCase depositUseCase;
    private WithdrawUseCase withdrawUseCase;
    private TransactionUseCase transactionUseCase;
    private WalletController walletController;
    private String user = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        getActualBalanceUseCase = mock(GetActualBalanceUseCase.class);
        depositUseCase = mock(DepositUseCase.class);
        withdrawUseCase = mock(WithdrawUseCase.class);
        transactionUseCase = mock(TransactionUseCase.class);
        walletController = new WalletController(
                getActualBalanceUseCase,
                depositUseCase,
                withdrawUseCase,
                transactionUseCase
        );
    }

    @Test
    void getBalance_shouldReturnBalanceResponse() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user);
        BalanceResponse balanceResponse = new BalanceResponse(BigDecimal.valueOf(1000.00));
        when(getActualBalanceUseCase.getActualBalance(any(UserId.class))).thenReturn(balanceResponse);

        ResponseEntity<BalanceResponse> response = walletController.getBalance(authentication);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(balanceResponse);
        ArgumentCaptor<UserId> captor = ArgumentCaptor.forClass(UserId.class);
        verify(getActualBalanceUseCase).getActualBalance(captor.capture());
    }

    @Test
    void deposit_shouldCallDepositUseCaseAndReturnOk() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user);
        WalletAmountRequest depositRequest = mock(WalletAmountRequest.class);
        when(depositRequest.amount()).thenReturn(BigDecimal.valueOf(500));

        ResponseEntity<BalanceResponse> response = walletController.deposit(authentication, depositRequest);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(depositUseCase).deposit(new UserId(user), BigDecimal.valueOf(500));
    }

    @Test
    void withdraw_shouldCallWithdrawUseCaseAndReturnOk() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user);
        WalletAmountRequest withdrawRequest = mock(WalletAmountRequest.class);
        when(withdrawRequest.amount()).thenReturn(BigDecimal.valueOf(200));

        ResponseEntity<BalanceResponse> response = walletController.withdraw(authentication, withdrawRequest);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(withdrawUseCase).withdraw(new UserId(user), BigDecimal.valueOf(200));
    }
    @Test
    void transaction_shouldCallTransactionUseCaseAndReturnOk() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user);
        WalletTransactionRequest transactionRequest = mock(WalletTransactionRequest.class);

        ResponseEntity<TransactionCreatedResponse> response = walletController.transaction(authentication, transactionRequest);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(transactionUseCase).transaction(new UserId(user), transactionRequest);
    }
}
