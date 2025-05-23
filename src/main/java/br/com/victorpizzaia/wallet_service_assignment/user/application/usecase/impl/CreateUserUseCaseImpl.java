package br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.impl;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.UseCase;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.user.application.service.PasswordEncoderService;
import br.com.victorpizzaia.wallet_service_assignment.user.application.usecase.CreateUserUseCase;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.User;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.UserRepository;
import jakarta.transaction.Transactional;

@UseCase
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final ApplicationEventPublisher eventPublisher;

    public CreateUserUseCaseImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public UserId createUser(String fullname, String cpf, String email, String plainPassword) {
        Optional<User> searchedUser = userRepository.findByCpf(cpf);
        if (searchedUser.isPresent()) {
            throw new IllegalArgumentException("User with CPF " + cpf + " already exists");
        }
        String hashedPassword = passwordEncoderService.hash(plainPassword);

        User newUser = new User(
            new UserId(),
            fullname,
            cpf,
            email,
            hashedPassword);

        userRepository.save(newUser);

        eventPublisher.publishEvent(new UserCreatedEvent(newUser.getId()));

        return newUser.getId();
    }
}
