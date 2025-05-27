package br.com.victorpizzaia.wallet_service_assignment.user.application.service.impl;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserService;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.exception.UserAlreadyExistException;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.User;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(String fullname, String cpf, String email, String plainPassword) {
        log.info("Starting process to create a new user: {}", cpf);
        Optional<User> searchedUser = userRepository.findByCpf(cpf);
        if (searchedUser.isPresent()) {
            throw new UserAlreadyExistException("User with CPF " + cpf + " already exists", 400);
        }
        log.info("Hashing password");
        String hashedPassword = passwordEncoderService.hash(plainPassword);

        User newUser = new User(
            new UserId(),
            fullname,
            cpf,
            email,
            hashedPassword);

        userRepository.save(newUser);
        log.info("Sucess on creating user: {}", newUser.getId());

        log.info("Sending event to create a wallet");
        eventPublisher.publishEvent(new UserCreatedEvent(newUser.getId()));
        return new CreateUserResponse(newUser.getId(), newUser.getFullname());
    }
}
