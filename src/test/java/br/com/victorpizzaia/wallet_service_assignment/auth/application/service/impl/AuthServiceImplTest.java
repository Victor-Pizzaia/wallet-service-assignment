package br.com.victorpizzaia.wallet_service_assignment.auth.application.service.impl;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.JwtProvider;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginRequest;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginResponse;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserNotFound;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserPasswordWrongException;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserQueryService;
import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private JwtProvider jwtProvider;
    private PasswordEncoderService passwordEncoderService;
    private UserQueryService userQueryService;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProvider.class);
        passwordEncoderService = mock(PasswordEncoderService.class);
        userQueryService = mock(UserQueryService.class);
        authService = new AuthServiceImpl(jwtProvider, passwordEncoderService, userQueryService);
    }

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreCorrect() {
        String identifier = "victor@email.com";
        String password = "password";
        String hashedPassword = "hashed";
        UserId userId = new UserId(UUID.randomUUID());
        String token = "jwt-token";
        long expiresIn = 3600L;

        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.identifier()).thenReturn(identifier);
        when(loginRequest.password()).thenReturn(password);

        UserCredentialsView user = mock(UserCredentialsView.class);
        when(user.getHashedPassword()).thenReturn(hashedPassword);
        when(user.getUserId()).thenReturn(userId.toString());

        when(userQueryService.findByEmailOrCpf(identifier)).thenReturn(Optional.of(user));
        when(passwordEncoderService.matches(password, hashedPassword)).thenReturn(true);
        when(jwtProvider.generateToken(userId.toString(), identifier)).thenReturn(token);
        when(jwtProvider.getExpiresIn()).thenReturn(expiresIn);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(token, response.token());
        assertEquals(expiresIn, response.expiresIn());
    }

    @Test
    void login_shouldThrowUserNotFound_whenUserDoesNotExist() {
        String identifier = "victor@email.com";
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.identifier()).thenReturn(identifier);

        when(userQueryService.findByEmailOrCpf(identifier)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_shouldThrowUserPasswordWrongException_whenPasswordIsIncorrect() {
        String identifier = "victor@email.com";
        String password = "wrongpassword";
        String hashedPassword = "hashed";
        UserId userId = new UserId(UUID.randomUUID());

        LoginRequest loginRequest = mock(LoginRequest.class);
        when(loginRequest.identifier()).thenReturn(identifier);
        when(loginRequest.password()).thenReturn(password);

        UserCredentialsView user = mock(UserCredentialsView.class);
        when(user.getHashedPassword()).thenReturn(hashedPassword);
        when(user.getUserId()).thenReturn(userId.toString());

        when(userQueryService.findByEmailOrCpf(identifier)).thenReturn(Optional.of(user));
        when(passwordEncoderService.matches(password, hashedPassword)).thenReturn(false);

        assertThrows(UserPasswordWrongException.class, () -> authService.login(loginRequest));
    }
}
