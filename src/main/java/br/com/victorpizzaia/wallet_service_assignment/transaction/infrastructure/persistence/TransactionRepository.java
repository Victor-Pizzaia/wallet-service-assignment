package br.com.victorpizzaia.wallet_service_assignment.transaction.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.TransactionId;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionId> {
}
