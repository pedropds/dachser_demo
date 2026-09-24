-- This table stores all valid information for the customers.
CREATE TABLE shipments
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(50) NOT NULL,
    customer_id     BIGINT      NOT NULL,
    status          VARCHAR(50) DEFAULT 'CREATED',
    description     TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);