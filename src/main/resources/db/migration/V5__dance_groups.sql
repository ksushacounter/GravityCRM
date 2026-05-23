CREATE TABLE dance_groups (
    group_id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    direction VARCHAR(50),
    level VARCHAR(50),
    choreographer_id INT REFERENCES choreographers(choreographer_id)
);