package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Schedule;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ScheduleRepository {
    @SqlUpdate("INSERT INTO schedule (group_id, hall_id, day_of_week, start_time, end_time)" +
    "VALUES(:groupId, :hallId,:dayOfWeek, :startTime, :endTime)")
    @GetGeneratedKeys("schedule_id")
    Long newClass(@BindMethods Schedule schedule);

    @SqlQuery("SELECT schedule_id, group_id, hall_id, day_of_week, start_time, end_time FROM schedule ORDER BY day_of_week, start_time")
    @RegisterConstructorMapper(Schedule.class)
    List<Schedule> findAll();

    @SqlUpdate("""
            WITH detached_attendance AS (
                UPDATE attendance SET schedule_id = NULL WHERE schedule_id = :scheduleId
            )
            DELETE FROM schedule WHERE schedule_id = :scheduleId
            """)
    int deleteById(@Bind("scheduleId") Long scheduleId);
}
