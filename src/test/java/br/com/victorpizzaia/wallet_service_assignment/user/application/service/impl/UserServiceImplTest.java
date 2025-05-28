package br.com.victorpizzaia.wallet_service_assignment.user.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.event.UserCreatedEvent;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.CreateUserResponse;
import br.com.victorpizzaia.wallet_service_assignment.user.domain.exception.UserAlreadyExistException;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.User;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoderService passwordEncoderService;
    private ApplicationEventPublisher eventPublisher;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoderService = mock(PasswordEncoderService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        userService = new UserServiceImpl(userRepository, passwordEncoderService, eventPublisher);
    }

    @Test
    void createUser_shouldCreateUserAndPublishEvent_whenCpfDoesNotExist() {
        String fullname = "Victor Hugo Pizzaia";
        String cpf = "12345678900";
        String email = "victor@example.com";
        String plainPassword = "password";
        String hashedPassword = "hashedPassword";

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(passwordEncoderService.hash(plainPassword)).thenReturn(hashedPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        CreateUserResponse response = userService.createUser(fullname, cpf, email, plainPassword);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(fullname, savedUser.getFullname());
        assertEquals(cpf, savedUser.getCpf());
        assertEquals(email, savedUser.getEmail());
        assertEquals(hashedPassword, savedUser.getPasswordHash());

        assertNotNull(response);
        assertEquals(savedUser.getId(), response.userId());
        assertEquals(savedUser.getFullname(), response.fullname());

        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(savedUser.getId(), eventCaptor.getValue().userId());
    }

    @Test
    void createUser_shouldThrowException_whenCpfAlreadyExists() {
        String fullname = "Bruna Mauri";
        String cpf = "12345678910";
        String email = "bruna@example.com";
        String plainPassword = "password";
        User existingUser = mock(User.class);

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.of(existingUser));

        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> userService.createUser(fullname, cpf, email, plainPassword)
        );
        assertTrue(exception.getMessage().contains("User with CPF " + cpf + " already exists"));
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
