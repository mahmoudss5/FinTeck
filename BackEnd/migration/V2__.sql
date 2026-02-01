ALTER TABLE transactions
    MODIFY created_at datetime NOT NULL;

ALTER TABLE users
    MODIFY created_at datetime NOT NULL;

ALTER TABLE roles
    MODIFY name VARCHAR (255);

ALTER TABLE transactions
    MODIFY status VARCHAR (255);

ALTER TABLE transactions
    MODIFY status VARCHAR (255) NOT NULL;