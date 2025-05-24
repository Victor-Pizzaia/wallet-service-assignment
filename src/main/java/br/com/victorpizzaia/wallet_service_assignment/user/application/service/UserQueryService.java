package br.com.victorpizzaia.wallet_service_assignment.user.application.service;

import java.util.Optional;

import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;

public interface UserQueryService {
    Optional<UserCredentialsView> findByEmailOrCpf(String identifier);
}
