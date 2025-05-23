package br.com.victorpizzaia.wallet_service_assignment.user.application.usecase;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

public interface CreateUserUseCase {
    UserId createUser(String fullname, String cpf, String email, String plainPassword);
}
