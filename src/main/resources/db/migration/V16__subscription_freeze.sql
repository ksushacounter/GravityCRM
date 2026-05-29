CREATE TABLE IF NOT EXISTS subscription_freezes (
    freeze_id SERIAL PRIMARY KEY,
    client_subscription_id INT REFERENCES subscriptions(sub_id) ON DELETE RESTRICT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    document_url VARCHAR(500),
    created_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT check_freeze_dates CHECK (start_date <= end_date)
);

CREATE OR REPLACE FUNCTION apply_subscription_freeze()
RETURNS TRIGGER AS $$
DECLARE
    freeze_days INT;
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM subscriptions
        WHERE sub_id = NEW.client_subscription_id
          AND status IN ('ACTIVE', 'FROZEN')
    ) THEN
        RAISE EXCEPTION 'Cannot freeze missing or expired subscription';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM subscription_freezes
        WHERE client_subscription_id = NEW.client_subscription_id
          AND freeze_id != COALESCE(NEW.freeze_id, -1)
          AND NEW.start_date <= end_date
          AND NEW.end_date >= start_date
    ) THEN
        RAISE EXCEPTION 'Freeze period overlaps an existing freeze';
    END IF;

    freeze_days := (NEW.end_date - NEW.start_date) + 1;

    UPDATE subscriptions
    SET end_date = end_date + freeze_days,
        status = CASE
            WHEN CURRENT_DATE BETWEEN NEW.start_date AND NEW.end_date THEN 'FROZEN'
            ELSE status
        END
    WHERE sub_id = NEW.client_subscription_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_apply_subscription_freeze ON subscription_freezes;

CREATE TRIGGER trigger_apply_subscription_freeze
BEFORE INSERT ON subscription_freezes
FOR EACH ROW
EXECUTE FUNCTION apply_subscription_freeze();
