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
    login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
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

INSERT INTO banks(name)
VALUES
    ('BelAgroPromBank'),
    ('Alfa Bank'),
    ('MTBank'),
    ('BelInvestBank'),
    ('ASB BelarusBank');

INSERT INTO currencies(name)
VALUES
    ('BYN'),
    ('EUR'),
    ('USD'),
    ('RUB'),
    ('UAH');

INSERT INTO transaction_types(name)
VALUES
    ('Перевод'),
    ('Снятие средств'),
    ('Пополнение счета'),
    ('Начисление процентов по счету'),
    ('Прочие расходы');

INSERT INTO users(name, login, password)
VALUES
    ('Сотников Кирилл Артёмович', 'kirill', 'password'),
    ('Ковалёв Сергей Николаевич', 'sergey', 'password'),
    ('Петров Николай Фиофанович', '123456', '789'),
    ('Орлова Ирина Олеговна', '111', '222'),
    ('Кондратова Анна Викторовна', '333', '444'),
    ('Шипенько Дарья Никифоровна', '555', '666'),
    ('Емельяненко Дмитрий Сапегович', '777', '888'),
    ('Климко Олег Сергеевич', '999', '000'),
    ('Домашенко Инга Фёдоровна', 'qwerty', 'qwerty'),
    ('Акрень Клим Дмитриевич', 'klim', 'klim'),
    ('Неугадайко Дарья Ильевна', 'dasha', 'dasha'),
    ('Догадайко Михаил Арсеньевич', 'misha', 'misha'),
    ('Разгадай Дмитрий Арсеньевич', 'ololo', 'tralala'),
    ('Алфёрова Данна Даниловна', 'hey', 'ho'),
    ('Укрупненко Михаил Егорович', 'big', 'man'),
    ('Замшелов Дмитрий Александрович', 'dimas', 'dimas'),
    ('Краснова Алина Артёмовна', 'red', 'alina'),
    ('Захудалов Михаил Дмитриевич', 'slim', 'man'),
    ('Закрутко Анна Леонидовна', 'anna', 'leonidovna'),
    ('Леонидов Михаил Джабулаевич', 'misha', 'dzhaba');

INSERT INTO accounts(name, opening_date, balance, user_id, bank_id, currency_id)
VALUES
    ('AS12 ASDG 1200 2132 ASDA 353A 2132', '1970-01-01', 10000, 1, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2248', '1980-12-30', 6544, 2, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2355', '2000-11-22', 12000, 3, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2466', '2001-10-21', 7300, 4, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2577', '2002-09-20', 1500, 5, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2688', '2003-08-19', 2800, 6, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 2999', '2004-07-18', 11000, 7, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3011', '2005-06-17', 9554, 8, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3122', '2006-05-16', 776, 9, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3233', '2007-04-15', 1200, 10, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3344', '2008-03-14', 7800, 11, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3566', '2009-02-13', 25, 12, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3677', '2010-01-12', 79, 13, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3788', '2011-12-11', 930, 14, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 3899', '2012-11-10', 68, 15, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 4122', '2013-10-09', 5200, 16, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 4233', '2014-09-08', 1300, 17, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 4344', '2015-08-07', 450, 18, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 4455', '2016-07-06', 356, 19, 1, 1),
    ('AS12 4306 1200 2132 DGSS 56SD 4566', '2017-06-05', 998, 20, 1, 1),
    ('ALFA 6654 AGRR 5566 OOPS AHHA 0001', '1999-10-25', 8008, 1, 2, 1),
    ('ALFA 6654 AGRR 5566 OOPS AHHA 0010', '2000-09-26', 7007, 7, 2, 1),
    ('ALFA 6654 AGRR 5566 OOPS AHHA 0011', '2001-08-27', 5005, 10, 2, 1),
    ('ALFA 6654 AGRR 5566 OOPS AHHA 0100', '2002-07-28', 3003, 11, 2, 1),
    ('ALFA 6654 AGRR 5566 OOPS AHHA 0101', '2003-06-29', 1001, 19, 2, 1),
    ('MTBB 0234 HEHE 4957 AGGA ROFL 1000', '2000-01-01', 1111, 10, 3, 1),
    ('MTBB 0234 HEHE 4957 AGGA ROFL 1001', '2002-12-02', 2222, 11, 3, 1),
    ('MTBB 0234 HEHE 4957 AGGA ROFL 1010', '2004-02-03', 3333, 12, 3, 1),
    ('MTBB 0234 HEHE 4957 AGGA ROFL 1011', '2008-11-04', 4444, 13, 3, 1),
    ('MTBB 0234 HEHE 4957 AGGA ROFL 1100', '2016-03-05', 5555, 14, 3, 1),
    ('BIBA 5464 LOL4 5566 FORI 1110 6543', '2010-12-12', 650, 1, 4, 1),
    ('BIBA 5464 LOL4 5566 FORI 1110 7890', '2010-12-13', 750, 2, 4, 1),
    ('BIBA 5464 LOL4 5566 FORI 1110 8901', '2010-12-14', 850, 3, 4, 1),
    ('BIBA 5464 LOL4 5566 FORI 1110 9012', '2010-12-15', 950, 4, 4, 1),
    ('BIBA 5464 LOL4 5566 FORI 1110 0123', '2010-12-16', 1050, 5, 4, 1),
    ('BRBA 2414 DORK 6678 EACH NAM3 3629', '2019-01-16', 635, 3, 5, 1),
    ('BRBA 2414 DORK 6678 EACH NAM3 1622', '2017-11-23', 9600, 18, 5, 1),
    ('BRBA 2414 DORK 6678 EACH NAM3 4982', '2021-05-06', 386, 5, 5, 1),
    ('BRBA 2414 DORK 6678 EACH NAM3 1863', '1983-11-11', 1100, 11, 5, 1),
    ('BRBA 2414 DORK 6678 EACH NAM3 6832', '1993-06-28', 932, 15, 5, 1);