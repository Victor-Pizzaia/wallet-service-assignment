package br.com.victorpizzaia.wallet_service_assignment.auth.application.service.impl;

import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.AuthService;
import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.JwtProvider;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginRequest;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginResponse;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserNotFound;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.exception.UserPasswordWrongException;
import br.com.victorpizzaia.wallet_service_assignment.user.application.service.UserQueryService;
import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final PasswordEncoderService passwordEncoderService;
    private final UserQueryService userQueryService;

    public AuthServiceImpl(JwtProvider jwtProvider, PasswordEncoderService passwordEncoderService, UserQueryService userQueryService) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoderService = passwordEncoderService;
        this.userQueryService = userQueryService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting to login user with identifier: {}", loginRequest.identifier());
        UserCredentialsView user = userQueryService.findByEmailOrCpf(loginRequest.identifier())
            .orElseThrow(() -> new UserNotFound("User %s not found".formatted(loginRequest.identifier())));

        if (!passwordEncoderService.matches(loginRequest.password(), user.getHashedPassword())) {
            throw new UserPasswordWrongException("Wrong password");
        }

        String token = jwtProvider.generateToken(user.getUserId().toString(), loginRequest.identifier());
        long expirationTime = jwtProvider.getExpiresIn();
        return new LoginResponse(token, expirationTime);
    }
}
