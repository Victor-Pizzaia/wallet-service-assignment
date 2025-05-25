package br.com.victorpizzaia.wallet_service_assignment.transaction.domain;

public enum TransactionStatus {
    PENDING("PENDING"), COMPLETED("COMPLETED"), FAILED("FAILED");

    private String value;

    private TransactionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
