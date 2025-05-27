package br.com.victorpizzaia.wallet_service_assignment.user.application.service;

import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(String fullname, String cpf, String email, String plainPassword);
}
