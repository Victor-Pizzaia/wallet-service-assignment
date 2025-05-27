package br.com.victorpizzaia.wallet_service_assignment.wallet.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.DepositUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.GetActualBalanceUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.application.usecase.WithdrawUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.BalanceResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.CreateWalletRequest;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.TransactionCreatedResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletAmountRequest;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.WalletTransactionRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/wallets")
@Validated
@Slf4j
public class WalletController {

    private final CreateUserUseCase createUserUseCase;
    private final GetActualBalanceUseCase getActualBalanceUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransactionUseCase transactionUseCase;

    public WalletController(
        CreateUserUseCase createUserUseCase,
        GetActualBalanceUseCase getActualBalanceUseCase,
        DepositUseCase depositUseCase,
        WithdrawUseCase withdrawUseCase,
        TransactionUseCase transactionUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getActualBalanceUseCase = getActualBalanceUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.transactionUseCase = transactionUseCase;
    }

    @PostMapping
    public ResponseEntity<UserId> createUserAndWallet(@Valid @RequestBody CreateWalletRequest createWalletRequest) {
        UserId userId = createUserUseCase.createUser(createWalletRequest.fullname(),
                createWalletRequest.cpf(), createWalletRequest.email(), createWalletRequest.plainPassword());
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(Authentication authentication) {
        log.info("New request to get balance received");
        String userId = authentication.getName();

        return ResponseEntity.ok(getActualBalanceUseCase.getActualBalance(new UserId(userId)));
    }

    @PostMapping("/deposit")
    public ResponseEntity<BalanceResponse> deposit(Authentication authentication, @Valid @RequestBody WalletAmountRequest depositRequest) {
        log.info("New request to deposit received");
        String userId = authentication.getName();

        return ResponseEntity.ok(depositUseCase.deposit(new UserId(userId), depositRequest.amount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BalanceResponse> withdraw(Authentication authentication, @Valid @RequestBody WalletAmountRequest withdrawRequest) {
        log.info("New request to withdraw received");
        String userId = authentication.getName();

        return ResponseEntity.ok(withdrawUseCase.withdraw(new UserId(userId), withdrawRequest.amount()));
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionCreatedResponse> transaction(Authentication authentication, @Valid @RequestBody WalletTransactionRequest walletTransactionRequest) {
        log.info("New request to transaction received");
        String userId = authentication.getName();

        return ResponseEntity.ok(transactionUseCase.transaction(new UserId(userId), walletTransactionRequest));
    }
}
