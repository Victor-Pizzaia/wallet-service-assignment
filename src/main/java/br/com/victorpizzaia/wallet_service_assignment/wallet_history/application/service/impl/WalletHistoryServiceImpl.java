package br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.shared.domain.WalletId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletHistoryResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.exception.InvalidDateFormatException;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistory;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.infrastructure.persistence.WalletHistoryRepository;
import jakarta.transaction.Transactional;

@Service
public class WalletHistoryServiceImpl implements WalletHistoryService {

    private final WalletHistoryRepository walletHistoryRepository;
    DateTimeFormatter formatter;

    public WalletHistoryServiceImpl(WalletHistoryRepository walletHistoryRepository) {
        this.walletHistoryRepository = walletHistoryRepository;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Override
    @Transactional
    public void recordWalletHistory(UserId userId, WalletId walletId, BigDecimal balance, BigDecimal amount, String transactionType) {
        WalletHistory walletHistory = new WalletHistory(userId, walletId, balance, amount, transactionType);
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public Page<WalletHistoryResponse> getWalletHistory(UserId userId, String startDate, String endDate, Pageable pageable) {
        LocalDateTime startDateTimeFormated = formatDate(startDate)
            .map(date -> date.atStartOfDay())
            .orElse(null);
        LocalDateTime endDateTimeFormated = formatDate(endDate)
            .map(date -> date.plusDays(1).atStartOfDay().minusNanos(1))
            .orElse(null);

            if (validateDates(startDateTimeFormated, endDateTimeFormated)) {
                return walletHistoryRepository.findByUserIdWithDate(userId, startDateTimeFormated, endDateTimeFormated, pageable).map(wallet -> WalletHistoryResponse.toResponse(wallet));
            }

            return walletHistoryRepository.findByUserId(userId, pageable).map(wallet -> WalletHistoryResponse.toResponse(wallet));
    }

    private boolean validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
        return startDate != null || endDate != null;
    }

    private Optional<LocalDate> formatDate(String date) {
        if (date != null) {
            try {
                return Optional.of(LocalDate.parse(date, formatter));
            } catch (DateTimeParseException e) {
                throw new InvalidDateFormatException("Field %s is in the wrong format. Use dd-MM-yyyy".formatted(date));
            }
        }
        return Optional.empty();
    }
}
