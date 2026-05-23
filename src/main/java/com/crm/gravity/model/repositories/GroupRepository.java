package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.DanceGroup;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods; // Изменили импорт для records
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface GroupRepository {

    @SqlUpdate("INSERT INTO dance_groups (name, direction, level, choreographer_id) " +
            "VALUES (:name, :direction, :level, :choreographerId)")
    @GetGeneratedKeys("group_id")
    Long createGroup(@BindMethods DanceGroup group);

    @SqlUpdate("INSERT INTO student_groups (group_id, student_id) " +
            "VALUES (:groupId, :studentId) ON CONFLICT DO NOTHING")
    void enrollStudent(@Bind("groupId") Long groupId, @Bind("studentId") Long studentId);
}