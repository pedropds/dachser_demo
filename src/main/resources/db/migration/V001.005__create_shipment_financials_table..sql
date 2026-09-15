-- This table stores individual profit/loss calculation records.
-- Each row represents the outcome of a specific calculation event
-- (a distinct set of income and costs processed at one time),
-- rather than the grand running total for the entire shipment.
CREATE TABLE shipment_financials
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipment_id    BIGINT         NOT NULL,
    income         DECIMAL(10, 2) NOT NULL,
    total_costs    DECIMAL(10, 2) NOT NULL,
    profit_or_loss DECIMAL(10, 2) NOT NULL,
    calculated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments (id)
);