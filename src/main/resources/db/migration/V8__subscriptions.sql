CREATE TABLE subscriptions (
    sub_id SERIAL PRIMARY KEY,
    sub_number VARCHAR(50) UNIQUE NOT NULL,
    student_id INT REFERENCES students(student_id),
    type_id INT REFERENCES sub_types(type_id),
    purchase_date DATE DEFAULT CURRENT_DATE,
    end_date DATE,
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'FROZEN', 'EXPIRED'))
);