package br.com.victorpizzaia.wallet_service_assignment.user.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserRequest;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUserAndWallet(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Create new user request received");
        return ResponseEntity.ok(createUserUseCase.createUser(createUserRequest));
    }
}
