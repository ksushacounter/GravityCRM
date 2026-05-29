package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Attedance;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface AttendanceRepository {

    @SqlQuery("SELECT COUNT(*) > 0 FROM subscriptions WHERE sub_id = :subId AND status = 'ACTIVE' AND visit_count > 0")
    boolean isSubscriptionValid(@Bind("subId") Long subId);

    @SqlUpdate("INSERT INTO attendance (sub_id, schedule_id) VALUES (:subId, :scheduleId)")
    void markVisit(@Bind("subId") Long subId, @Bind("scheduleId") Long scheduleId);

    @SqlQuery("SELECT visit_id, sub_id, schedule_id, visit_date, is_extra, is_single FROM attendance ORDER BY visit_id DESC")
    @RegisterConstructorMapper(Attedance.class)
    List<Attedance> findAll();
}
