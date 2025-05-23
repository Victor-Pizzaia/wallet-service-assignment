package br.com.victorpizzaia.wallet_service_assignment.user.application.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.user.application.service.PasswordEncoderService;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matches'");
    }
    
}
