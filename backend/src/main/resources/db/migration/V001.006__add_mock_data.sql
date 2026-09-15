-- 1. Insert Test Customers
INSERT INTO customers (name, tax_id)
VALUES ('Acme Corp', 'US123456789'),
       ('Stark Industries', 'US987654321'),
       ('Wayne Enterprises', 'US555666777');

-- 2. Insert Test Shipments
INSERT INTO shipments (tracking_number, customer_id, status)
VALUES ('0001', 1, 'DELIVERED'),
       ('0002', 1, 'IN_TRANSIT'),
       ('0003', 1, 'CREATED'),
       ('0004', 2, 'DELIVERED'),
       ('0005', 2, 'IN_TRANSIT'),
       ('0006', 3, 'CREATED');