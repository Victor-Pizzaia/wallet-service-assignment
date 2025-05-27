package br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.impl;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserService;
import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserRequest;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;

@UseCase
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserService userService;

    public CreateUserUseCaseImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest.fullname(), createUserRequest.cpf(), createUserRequest.email(), createUserRequest.plainPassword());
    }
}
