package br.com.victorpizzaia.wallet_service_assignment.auth.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderServiceImplTest {

    private PasswordEncoderServiceImpl passwordEncoderService;

    @BeforeEach
    void setUp() {
        passwordEncoderService = new PasswordEncoderServiceImpl();
    }

    @Test
    void testMatchesWithCorrectPassword() {
        String plainPassword = "mySecret123!";
        String hashedPassword = passwordEncoderService.hash(plainPassword);

        assertTrue(passwordEncoderService.matches(plainPassword, hashedPassword));
    }

    @Test
    void testMatchesWithIncorrectPassword() {
        String plainPassword = "mySecret123!";
        String wrongPassword = "wrongPassword!";
        String hashedPassword = passwordEncoderService.hash(plainPassword);

        assertFalse(passwordEncoderService.matches(wrongPassword, hashedPassword));
    }

    @Test
    void testMatchesWithNullHashedPassword() {
        String plainPassword = "password";
        assertFalse(passwordEncoderService.matches(plainPassword, null));
    }

    @Test
    void testMatchesWithEmptyPassword() {
        String plainPassword = "";
        String hashedPassword = passwordEncoderService.hash(plainPassword);

        assertTrue(passwordEncoderService.matches(plainPassword, hashedPassword));
        assertFalse(passwordEncoderService.matches("notEmpty", hashedPassword));
    }
}
