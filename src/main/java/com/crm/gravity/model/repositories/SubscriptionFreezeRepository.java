package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.SubscriptionFreeze;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface SubscriptionFreezeRepository {

    @SqlUpdate("INSERT INTO subscription_freezes (client_subscription_id, start_date, end_date, reason, document_url) " +
            "VALUES (:clientSubscriptionId, :startDate, :endDate, :reason, :documentUrl)")
    void save(@BindMethods SubscriptionFreeze freeze);

    @SqlQuery("SELECT freeze_id, client_subscription_id, start_date, end_date, reason, document_url, created_at FROM subscription_freezes ORDER BY freeze_id DESC")
    @RegisterConstructorMapper(SubscriptionFreeze.class)
    List<SubscriptionFreeze> findAll();
}
