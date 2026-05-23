package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Schedule;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ScheduleRepository {
    @SqlUpdate("INSERT INTO schedule (group_id, hall_id, day_of_week, start_time, end_time)" +
    "VALUES(:groupId, :hallId,:dayOfWeek, :startTime, :endTime)")
    @GetGeneratedKeys("schedule_id")
    Long newClass(@BindMethods Schedule schedule);
}
