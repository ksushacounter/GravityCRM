package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Student;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface StudentRepository {

    @SqlUpdate("INSERT INTO students (fio, phone, status) " +
            "VALUES (:fio, :phone, :status)")
    @GetGeneratedKeys("student_id")
    Long createStudent(@BindMethods Student student);
}