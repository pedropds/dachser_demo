-- This table handles the customer income administration.
-- It directly stores customer payments related to a specific shipment.
CREATE TABLE incomes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipment_id BIGINT         NOT NULL,
    amount      DECIMAL(10, 2) NOT NULL,
    status      VARCHAR(20) DEFAULT 'ACTIVE', -- e.g., 'ACTIVE', 'VOIDED'
    description TEXT,
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments (id)
);