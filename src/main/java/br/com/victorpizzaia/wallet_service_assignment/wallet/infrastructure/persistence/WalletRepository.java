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
}
