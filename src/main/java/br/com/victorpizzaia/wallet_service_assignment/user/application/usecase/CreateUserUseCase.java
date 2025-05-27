package br.com.victorpizzaia.wallet_service_assignment.user.application.usecase;

import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserRequest;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;

public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
}
