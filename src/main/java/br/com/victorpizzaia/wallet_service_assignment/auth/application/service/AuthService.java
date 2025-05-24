package br.com.victorpizzaia.wallet_service_assignment.auth.application.service;

import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginRequest;
import br.com.victorpizzaia.wallet_service_assignment.auth.domain.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
