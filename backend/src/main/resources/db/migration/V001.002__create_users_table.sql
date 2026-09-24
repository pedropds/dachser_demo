-- Stores all the users of the system, including operators and administrators.
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    role       VARCHAR(50)  DEFAULT 'OPERATOR',
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);