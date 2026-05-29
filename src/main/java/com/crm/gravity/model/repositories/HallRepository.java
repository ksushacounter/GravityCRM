package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Halls;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface HallRepository {

    @SqlUpdate("INSERT INTO halls (name, capacity)" +
            "VALUES(:name, :capacity)")
    @GetGeneratedKeys("hall_id")
    Long newHall(@BindMethods Halls halls);

    @SqlQuery("SELECT hall_id AS id, name, capacity FROM halls ORDER BY hall_id DESC")
    @RegisterConstructorMapper(Halls.class)
    List<Halls> findAll();
}
