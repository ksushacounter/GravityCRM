package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Halls;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface HallRepository {

    @SqlUpdate("INSERT INTO halls (name, capacity)" +
            "VALUES(:name, :capacity)")
    @GetGeneratedKeys("hall_id")
    Long newHall(@BindMethods Halls halls);
}
