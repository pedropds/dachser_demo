-- 1. Insert Test Customers
INSERT INTO customers (name, tax_id)
VALUES ('Acme Corp', 'US123456789'),
       ('Stark Industries', 'US987654321'),
       ('Wayne Enterprises', 'US555666777');

-- 2. Insert Test Shipments
INSERT INTO shipments (tracking_number, customer_id, status, description)
VALUES ('0001', 1, 'DELIVERED', 'Shipment 0001 for Acme Corp'),
       ('0002', 1, 'IN_TRANSIT', 'Shipment 0002 for Acme Corp'),
       ('0003', 1, 'CREATED', 'Shipment 0003 for Acme Corp'),
       ('0004', 2, 'DELIVERED', 'Shipment 0004 for Stark Industries'),
       ('0005', 2, 'IN_TRANSIT', 'Shipment 0005 for Stark Industries'),
       ('0006', 3, 'CREATED', 'Shipment 0006 for Wayne Enterprises');