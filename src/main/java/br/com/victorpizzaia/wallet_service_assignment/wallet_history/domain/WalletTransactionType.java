package br.com.victorpizzaia.wallet_service_assignment.wallet_history.domain;

public enum WalletTransactionType {
    DEPOSIT("DEPOSIT"), WITHDRAW("WITHDRAW");

    private final String value;

    WalletTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
