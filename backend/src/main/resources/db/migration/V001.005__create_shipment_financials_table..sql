-- This table stores individual profit/loss calculation records.
-- Each row represents the outcome of a specific calculation event.
CREATE TABLE shipment_financials
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipment_id    BIGINT         NOT NULL,

    -- Audit Links
    income_ids     BIGINT ARRAY,
    cost_ids       BIGINT ARRAY,

    -- Calculated Values
    income         DECIMAL(10, 2) NOT NULL,
    total_costs    DECIMAL(10, 2) NOT NULL,
    profit_or_loss DECIMAL(10, 2) NOT NULL,
    calculated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (shipment_id) REFERENCES shipments (id)
);