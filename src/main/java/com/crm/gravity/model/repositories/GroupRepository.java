package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.DanceGroup;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface GroupRepository {

    @SqlUpdate("INSERT INTO dance_groups (name, direction, level, choreographer_id) " +
            "VALUES (:name, :direction, :level, :choreographerId)")
    @GetGeneratedKeys("group_id")
    Long createGroup(@BindMethods DanceGroup group);

    @SqlUpdate("INSERT INTO student_groups (group_id, student_id) " +
            "VALUES (:groupId, :studentId) ON CONFLICT DO NOTHING")
    void enrollStudent(@Bind("groupId") Long groupId, @Bind("studentId") Long studentId);

    @SqlQuery("SELECT group_id, name, direction, level, choreographer_id FROM dance_groups ORDER BY group_id DESC")
    @RegisterConstructorMapper(DanceGroup.class)
    List<DanceGroup> findAll();
}
