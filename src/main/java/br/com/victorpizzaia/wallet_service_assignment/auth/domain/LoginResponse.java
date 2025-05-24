package br.com.victorpizzaia.wallet_service_assignment.auth.domain;

public record LoginResponse(String token, long expiresIn) {

}
