package br.com.victorpizzaia.wallet_service_assignment.transaction.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.transaction.application.usecase.TransactionUseCase;
import br.com.victorpizzaia.wallet_service_assignment.transaction.domain.CreateTransactionRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    public TransactionController(TransactionUseCase transactionUseCase) {
        this.transactionUseCase = transactionUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createUserAndWallet(Authentication authentication, @Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        UserId userId = new UserId(authentication.getName());
        transactionUseCase.createTransaction(userId, createTransactionRequest);
        return ResponseEntity.ok().build();
    }
}
