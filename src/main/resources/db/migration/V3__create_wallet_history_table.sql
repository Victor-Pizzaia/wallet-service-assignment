CREATE TABLE WALLETS_HISTORY (
    id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    user_id UUID NOT NULL,
    balance DECIMAL(19, 4) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,

    CONSTRAINT fk_wallet_history_wallet
        FOREIGN KEY (wallet_id) REFERENCES WALLETS(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_wallet_history_user
        FOREIGN KEY (user_id) REFERENCES USERS(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_wallets_history_wallet_id ON WALLETS_HISTORY(wallet_id);
CREATE INDEX idx_wallets_history_user_id ON WALLETS_HISTORY(user_id);
