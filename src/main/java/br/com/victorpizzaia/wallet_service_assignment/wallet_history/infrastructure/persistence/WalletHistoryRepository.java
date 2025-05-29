package br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletHistoryId;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, WalletHistoryId> {

    Page<WalletHistory> findByUserId(UserId userId, Pageable pageable);

    @Query("""
        SELECT wh FROM WalletHistory wh
        WHERE wh.userId = :userId
        AND wh.createdAt >= COALESCE(:startDate, wh.createdAt)
        AND wh.createdAt <= COALESCE(:endDate, wh.createdAt)
    """)
    Page<WalletHistory> findByUserIdWithDate(@Param("userId") UserId userId, @Param("startDate") LocalDateTime startDateTimeFormated,
            @Param("endDate") LocalDateTime endDateTimeFormated, Pageable pageable);
}
