package br.com.victorpizzaia.wallet_service_assignment.wallet.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, WalletId> {
}
