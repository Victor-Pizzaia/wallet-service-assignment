package br.com.victorpizzaia.wallet_service_assignment.shared.domain.event;

import java.io.Serializable;

import org.springframework.modulith.events.Externalized;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

@Externalized
public record UserCreatedEvent(UserId userId) implements Serializable{

}
