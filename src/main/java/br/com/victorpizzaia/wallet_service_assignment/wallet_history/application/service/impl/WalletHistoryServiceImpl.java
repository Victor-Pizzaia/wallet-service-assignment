package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletHistoryResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistory;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistoryRepository;
import jakarta.transaction.Transactional;

@Service
public class WalletHistoryServiceImpl implements WalletHistoryService {

    private final WalletHistoryRepository walletHistoryRepository;

    public WalletHistoryServiceImpl(WalletHistoryRepository walletHistoryRepository) {
        this.walletHistoryRepository = walletHistoryRepository;
    }

    @Override
    @Transactional
    public void recordWalletHistory(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType) {
        WalletHistory walletHistory = new WalletHistory(userId, walletId, balance, amount, transactionType);
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public Page<WalletHistoryResponse> getWalletHistory(UserId userId, Pageable pageable) {
        return walletHistoryRepository.findByUserId(userId, pageable).map(wallet -> WalletHistoryResponse.toResponse(wallet));
    }
}
