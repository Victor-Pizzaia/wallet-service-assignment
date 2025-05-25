package br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletHistoryId;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, WalletHistoryId> {
    Page<WalletHistory> findByUserId(UserId userId, Pageable pageable);
}
