CREATE OR REPLACE FUNCTION check_schedule_conflicts()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM schedule
        WHERE hall_id = NEW.hall_id
          AND day_of_week = NEW.day_of_week
          AND schedule_id != COALESCE(NEW.schedule_id, -1)
          AND NEW.start_time < end_time
          AND NEW.end_time > start_time
    ) THEN
        RAISE EXCEPTION 'Конфликт расписания: Этот зал уже занят другой группой на это время!';
    END IF;

    IF EXISTS (
        SELECT 1 FROM schedule s
        JOIN dance_groups dg_current ON dg_current.group_id = NEW.group_id
        JOIN dance_groups dg_existing ON dg_existing.group_id = s.group_id
        WHERE dg_existing.choreographer_id = dg_current.choreographer_id
          AND s.day_of_week = NEW.day_of_week
          AND s.schedule_id != COALESCE(NEW.schedule_id, -1)
          AND NEW.start_time < s.end_time
          AND NEW.end_time > s.start_time
    ) THEN
        RAISE EXCEPTION 'Конфликт расписания: Хореограф уже ведет другое занятие в это время!';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS validate_schedule_trigger ON schedule;

CREATE TRIGGER validate_schedule_trigger
BEFORE INSERT OR UPDATE ON schedule
FOR EACH ROW
EXECUTE FUNCTION check_schedule_conflicts();