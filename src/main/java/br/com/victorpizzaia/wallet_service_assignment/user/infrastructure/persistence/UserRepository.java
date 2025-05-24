package br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.user.application.view.UserCredentialsView;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findByCpf(String cpf);

    @Query("SELECT u.id.id AS userId, u.passwordHash AS hashedPassword FROM User u WHERE u.email = :identifier OR u.cpf = :identifier")
    Optional<UserCredentialsView> findPasswordHashByEmailOrCpf(@Param("identifier") String identifier);
}
