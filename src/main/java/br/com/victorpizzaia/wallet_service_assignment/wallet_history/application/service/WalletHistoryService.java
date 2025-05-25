package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletHistoryResponse;

public interface WalletHistoryService {
    void recordWalletHistory(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType);
    Page<WalletHistoryResponse> getWalletHistory(UserId userId, Pageable pageable);
}
