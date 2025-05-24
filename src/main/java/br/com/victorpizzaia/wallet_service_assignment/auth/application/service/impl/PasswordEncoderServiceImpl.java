package br.com.victorpizzaia.wallet_service_assignment.auth.application.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;

@Service
public class PasswordEncoderServiceImpl implements PasswordEncoderService {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderServiceImpl() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    @Override
    public boolean matches(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
    
}
