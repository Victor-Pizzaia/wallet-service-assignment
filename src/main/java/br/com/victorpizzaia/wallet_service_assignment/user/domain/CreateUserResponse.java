package br.com.victorpizzaia.wallet_service_assignment.user.domain;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

public record CreateUserResponse(
    UserId userId,
    String fullname
) {

}
