package br.com.victorpizzaia.wallet_service_assignment.wallet_history.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.victorpizzaia.wallet_service_assignment.shared.domain.UserId;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.application.service.WalletHistoryService;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.PageResponse;
import br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain.WalletHistoryResponse;

@RestController
@RequestMapping("/statement")
@Validated
public class WalletHistoryController {

    private final WalletHistoryService walletHistoryService;

    public WalletHistoryController(WalletHistoryService walletHistoryService) {
        this.walletHistoryService = walletHistoryService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<WalletHistoryResponse>> getStatement(Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {
        String userId = authentication.getName();

        Page<WalletHistoryResponse> walletHistoryResponse = walletHistoryService.getWalletHistory(new UserId(userId), pageable);
        return ResponseEntity.ok(PageResponse.from(walletHistoryResponse));
    }
}
