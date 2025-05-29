package br.com.victorpizzaia.wallet_service_assignment.wallet_history.web;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.PageResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletHistoryResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletTransactionType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WalletHistoryControllerTest {

    private WalletHistoryService walletHistoryService;
    private WalletHistoryController controller;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        walletHistoryService = mock(WalletHistoryService.class);
        controller = new WalletHistoryController(walletHistoryService);
        authentication = mock(Authentication.class);
    }

    @Test
    void getStatement_shouldReturnPageResponse() {
        String userId = UUID.randomUUID().toString();
        when(authentication.getName()).thenReturn(userId);

        Pageable pageable = PageRequest.of(0, 10);
        WalletHistoryResponse response = new WalletHistoryResponse(new BigDecimal(100), new BigDecimal(10), WalletTransactionType.DEPOSIT, LocalDateTime.now());
        List<WalletHistoryResponse> content = Collections.singletonList(response);
        Page<WalletHistoryResponse> page = new PageImpl<>(content, pageable, 1);

        when(walletHistoryService.getWalletHistory(any(UserId.class), any(String.class), any(String.class), any(Pageable.class))).thenReturn(page);

        ResponseEntity<PageResponse<WalletHistoryResponse>> result = controller.getStatement(authentication, "28-05-2025", "28-05-2025", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().content()).hasSize(1);

        ArgumentCaptor<UserId> userIdCaptor = ArgumentCaptor.forClass(UserId.class);
        ArgumentCaptor<String> startDateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> endDateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(walletHistoryService).getWalletHistory(userIdCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), pageableCaptor.capture());
        assertThat(userIdCaptor.getValue().id().toString()).isEqualTo(userId);
        assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
    }
}
