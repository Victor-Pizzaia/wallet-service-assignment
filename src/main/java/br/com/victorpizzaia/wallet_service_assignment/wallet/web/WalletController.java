package br.com.victorpizzaia.wallet_service_assignment.wallet.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.wallet.domain.CreateWalletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallets")
@Validated
public class WalletController {

    private final CreateUserUseCase createUserUseCase;

    public WalletController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserId> createUserAndWallet(@Valid @RequestBody CreateWalletRequest createWalletRequest) {
        UserId userId = createUserUseCase.createUser(createWalletRequest.fullname(),
                createWalletRequest.cpf(), createWalletRequest.email(), createWalletRequest.plainPassword());
        return ResponseEntity.ok(userId);
    }
}
