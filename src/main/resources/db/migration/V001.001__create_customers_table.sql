-- This table stores the client information.
CREATE TABLE customers
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    tax_id VARCHAR(50)
);