package br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findByCpf(String cpf);
}
