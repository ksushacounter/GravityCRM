package com.crm.gravity.model.repositories;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AttendanceRepository {

    @SqlQuery("SELECT COUNT(*) > 0 FROM subscriptions WHERE sub_id = :subId AND status = 'ACTIVE' AND visit_count > 0")
    boolean isSubscriptionValid(@Bind("subId") Long subId);

    @SqlUpdate("INSERT INTO attendance (sub_id, schedule_id) VALUES (:subId, :scheduleId)")
    void markVisit(@Bind("subId") Long subId, @Bind("scheduleId") Long scheduleId);
}