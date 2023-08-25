DROP TABLE IF EXISTS transaction_types;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS banks;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS currencies;



CREATE TABLE banks (
    id SERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users (
    id SERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE currencies (
    id SERIAL NOT NULL,
    name VARCHAR(3) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE transaction_types (
    id SERIAL NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE accounts (
    id SERIAL NOT NULL,
    name VARCHAR(34) NOT NULL,
    opening_date TIMESTAMP without TIME ZONE,
    balance INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    bank_id INTEGER NOT NULL,
    currency_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY(bank_id) REFERENCES banks(id) ON DELETE RESTRICT,
    FOREIGN KEY(currency_id) REFERENCES currencies(id) ON DELETE RESTRICT
);

CREATE TABLE transactions (
    id SERIAL NOT NULL,
    transaction_date TIMESTAMP without TIME ZONE,
    transaction_type_id INTEGER NOT NULL,
    sender_account_id INTEGER,
    recipient_account_id INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (sender_account_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (recipient_account_id) REFERENCES users(id) ON DELETE RESTRICT
);