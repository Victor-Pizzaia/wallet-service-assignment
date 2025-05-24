package br.com.victorpizzaia.wallet_service_assignment.user.application.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserQueryService;
import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.UserRepository;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserCredentialsView> findByEmailOrCpf(String identifier) {
        return userRepository.findPasswordHashByEmailOrCpf(identifier);
    }
}
