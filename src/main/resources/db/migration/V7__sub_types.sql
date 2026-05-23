CREATE TABLE sub_types (
    type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    visit_count INT NOT NULL,
    sub_kind VARCHAR(50)
);