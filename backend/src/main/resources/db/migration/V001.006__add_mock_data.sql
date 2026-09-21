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

-- Shipment 1 (Tracking 0001) - Adding a second historical calculation (adjustment)
INSERT INTO incomes (shipment_id, amount, description) VALUES (1, 150.00, 'Late fee adjustment');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (1, 'ADDITIONAL', 50.00, 'Extra handling');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (1, 'Post-delivery adjustment', ARRAY[3], ARRAY[3], 1150.00, 250.00, 900.00);

-- Shipment 2 (Tracking 0002) - Acme Corp (IN_TRANSIT)
INSERT INTO incomes (shipment_id, amount, description) VALUES (2, 1200.00, 'Standard freight');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (2, 'BASE', 400.00, 'Carrier cost');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (2, 'ADDITIONAL', 100.00, 'Fuel surcharge');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (2, 'Initial transit calculation', ARRAY[4], ARRAY[4, 5], 1200.00, 500.00, 700.00);

-- Shipment 3 (Tracking 0003) - Acme Corp (CREATED)
INSERT INTO incomes (shipment_id, amount, description) VALUES (3, 850.00, 'Base rate quote');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (3, 'BASE', 300.00, 'Estimated carrier cost');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (3, 'Pre-shipment estimate', ARRAY[5], ARRAY[6], 850.00, 300.00, 550.00);

-- Shipment 4 (Tracking 0004) - Adding a second historical calculation to Stark Industries
INSERT INTO incomes (shipment_id, amount, description) VALUES (4, 1000.00, 'Service recovery credit');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (4, 'Service recovery adjustment', ARRAY[2, 6], ARRAY[2], 1500.00, 900.00, 600.00);

-- Shipment 5 (Tracking 0005) - Stark Industries (IN_TRANSIT)
INSERT INTO incomes (shipment_id, amount, description) VALUES (5, 2200.00, 'Armored transport');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (5, 'BASE', 1500.00, 'Security personnel');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (5, 'High-security transit calculation', ARRAY[7], ARRAY[7], 2200.00, 1500.00, 700.00);

-- Shipment 6 (Tracking 0006) - Wayne Enterprises (CREATED)
INSERT INTO incomes (shipment_id, amount, description) VALUES (6, 3500.00, 'Express international');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (6, 'BASE', 2000.00, 'Air freight');
INSERT INTO costs (shipment_id, cost_type, amount, description) VALUES (6, 'ADDITIONAL', 250.00, 'Customs clearance');
INSERT INTO shipment_financials (shipment_id, description, income_ids, cost_ids, income, total_costs, profit_or_loss)
VALUES (6, 'International estimate', ARRAY[8], ARRAY[8, 9], 3500.00, 2250.00, 1250.00);