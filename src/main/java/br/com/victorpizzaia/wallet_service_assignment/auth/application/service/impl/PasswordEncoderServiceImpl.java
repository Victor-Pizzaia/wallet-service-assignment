package br.com.victorpizzaia.wallet_service_assignment.auth.application.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.auth.application.service.PasswordEncoderService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasswordEncoderServiceImpl implements PasswordEncoderService {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderServiceImpl() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hash(String plainPassword) {
        log.info("Hashing password");
        return encoder.encode(plainPassword);
    }

    @Override
    public boolean matches(String plainPassword, String hashedPassword) {
        log.info("Checking if plain password matches hashed password");
        return encoder.matches(plainPassword, hashedPassword);
    }
}
