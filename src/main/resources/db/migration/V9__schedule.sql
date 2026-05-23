CREATE TABLE schedule (
    schedule_id SERIAL PRIMARY KEY,
    group_id INT REFERENCES dance_groups(group_id),
    hall_id INT REFERENCES halls(hall_id),
    day_of_week INT CHECK (day_of_week BETWEEN 1 AND 7),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT check_times CHECK (start_time < end_time)
);