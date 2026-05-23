package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Choreographer;
import org.jdbi.v3.sqlobject.customizer.BindMethods; // Изменили импорт
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ChoreographerRepository {

    @SqlUpdate("INSERT INTO choreographers (fio) " +
            "VALUES (:fio)")
    @GetGeneratedKeys("choreographer_id")
    Long createChoreographer(@BindMethods Choreographer choreographer);
}