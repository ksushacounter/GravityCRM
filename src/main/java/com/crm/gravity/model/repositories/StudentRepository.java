package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Student;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface StudentRepository {

    @SqlUpdate("INSERT INTO students (fio, phone, status) " +
            "VALUES (:fio, :phone, :status)")
    @GetGeneratedKeys("student_id")
    Long createStudent(@BindMethods Student student);

    @SqlQuery("SELECT student_id, fio, phone, status FROM students ORDER BY student_id DESC")
    @RegisterConstructorMapper(Student.class)
    List<Student> findAll();

    @SqlQuery("SELECT COUNT(*) > 0 FROM subscriptions WHERE student_id = :studentId AND status IN ('ACTIVE', 'FROZEN')")
    boolean hasActiveSubscription(@Bind("studentId") Long studentId);

    @SqlUpdate("""
            WITH detached_subscriptions AS (
                UPDATE subscriptions SET student_id = NULL WHERE student_id = :studentId
            ),
            removed_group_links AS (
                DELETE FROM student_groups WHERE student_id = :studentId
            )
            DELETE FROM students WHERE student_id = :studentId
            """)
    int deleteById(@Bind("studentId") Long studentId);
}
