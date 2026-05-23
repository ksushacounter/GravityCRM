CREATE TABLE student_groups (
    group_id INT REFERENCES dance_groups(group_id),
    student_id INT REFERENCES students(student_id),
    PRIMARY KEY (group_id, student_id)
);