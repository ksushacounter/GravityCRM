package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Choreographer;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ChoreographerRepository {

    @SqlUpdate("INSERT INTO choreographers (fio) " +
            "VALUES (:fio)")
    @GetGeneratedKeys("choreographer_id")
    Long createChoreographer(@BindMethods Choreographer choreographer);

    @SqlQuery("SELECT choreographer_id, fio FROM choreographers ORDER BY choreographer_id DESC")
    @RegisterConstructorMapper(Choreographer.class)
    List<Choreographer> findAll();
}
