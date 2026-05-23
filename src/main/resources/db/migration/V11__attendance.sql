CREATE TABLE attendance (
    visit_id SERIAL PRIMARY KEY,
    sub_id INT REFERENCES subscriptions(sub_id),
    schedule_id INT REFERENCES schedule(schedule_id),
    visit_date DATE DEFAULT CURRENT_DATE,
    is_extra BOOLEAN DEFAULT FALSE,
    is_single BOOLEAN DEFAULT FALSE
);