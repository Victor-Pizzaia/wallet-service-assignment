package br.com.victorpizzaia.wallet_service_assignment.user.infrastructure.persistence;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private UserId id;
    @Column(nullable = false)
    private String fullname;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String passwordHash;
}
