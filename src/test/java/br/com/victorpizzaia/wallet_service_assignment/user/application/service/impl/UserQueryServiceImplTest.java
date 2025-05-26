package br.com.victorpizzaia.wallet_service_assignment.user.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;
import br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserQueryServiceImplTest {

    private UserRepository userRepository;
    private UserQueryServiceImpl userQueryService;
    private UserCredentialsView userCredentialsView;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userQueryService = new UserQueryServiceImpl(userRepository);
        userCredentialsView = mock(UserCredentialsView.class);
    }

    @Test
    void testFindByEmailOrCpf_ReturnsUserCredentialsView() {
        String identifier = "test@example.com";
        when(userRepository.findPasswordHashByEmailOrCpf(identifier))
                .thenReturn(Optional.of(userCredentialsView));

        Optional<UserCredentialsView> result = userQueryService.findByEmailOrCpf(identifier);

        assertTrue(result.isPresent());
        assertEquals(userCredentialsView, result.get());
        verify(userRepository, times(1)).findPasswordHashByEmailOrCpf(identifier);
    }

    @Test
    void testFindByEmailOrCpf_ReturnsEmptyOptional() {
        String identifier = "notfound@example.com";
        when(userRepository.findPasswordHashByEmailOrCpf(identifier))
                .thenReturn(Optional.empty());

        Optional<UserCredentialsView> result = userQueryService.findByEmailOrCpf(identifier);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findPasswordHashByEmailOrCpf(identifier);
    }
}
