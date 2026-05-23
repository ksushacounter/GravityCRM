CREATE TABLE halls (
    hall_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0)
);