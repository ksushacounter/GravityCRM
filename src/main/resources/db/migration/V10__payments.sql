CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fiscal_sign VARCHAR(100),
    admin_id INT REFERENCES admin(id),
    sub_id INT REFERENCES subscriptions(sub_id),
    status VARCHAR(20) DEFAULT 'PENDING'
);