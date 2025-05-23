package br.com.victorpizzaia.wallet_service_assignment.user.application.service;

public interface PasswordEncoderService {
    String hash(String plainPassword);
    boolean matches(String plainPassword, String hashedPassword);
}
