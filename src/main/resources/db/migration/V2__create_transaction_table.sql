CREATE TABLE TRANSACTIONS (
    id UUID PRIMARY KEY,
    payer_id UUID NOT NULL,
    payee_id UUID NOT NULL,
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    status VARCHAR(20) NOT NULL,
    details TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_payer_id
        FOREIGN KEY (payer_id) REFERENCES wallets (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_payee_id
        FOREIGN KEY (payee_id) REFERENCES wallets (id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_different_wallets
        CHECK (payer_id <> payee_id)
);
