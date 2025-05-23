-- Create table for Users
CREATE TABLE USERS (
    id UUID PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create table for Wallets
-- Each wallet is associated with a user by user_id
CREATE TABLE WALLETS (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL,
    balance DECIMAL(19, 4) DEFAULT 0.0000 NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
);
