package br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, WalletId> {

    @Query("SELECT w.balance FROM Wallet w WHERE w.userId = :userId")
    Optional<BigDecimal> findBalanceByUserId(@Param("userId") UserId userId);

    Optional<Wallet> findByUserId(UserId userId);

    @Query("""
        SELECT w FROM Wallet w
        JOIN User u ON u.id = w.userId
        WHERE (:payeeKey IS NOT NULL AND u.email = :payeeKey)
           OR (:payeeKey IS NOT NULL AND u.cpf = :payeeKey)
    """)
    Optional<Wallet> findByUserKey(@Param("payeeKey") String payeeKey);
}
