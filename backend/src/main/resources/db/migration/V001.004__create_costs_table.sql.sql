--This table tracks all operational costs related to service provision.
-- The cost_type allows us to distinguish between base costs and additional costs.
CREATE TABLE costs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipment_id BIGINT         NOT NULL,
    cost_type   VARCHAR(50)    NOT NULL,
    amount      DECIMAL(10, 2) NOT NULL,
    status      VARCHAR(20) DEFAULT 'ACTIVE', -- e.g., 'ACTIVE', 'VOIDED'
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments (id)
);