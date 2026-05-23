package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.SubscriptionFreeze;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SubscriptionFreezeRepository {

    @SqlUpdate("INSERT INTO subscription_freezes (client_subscription_id, start_date, end_date, reason, document_url) " +
            "VALUES (:clientSubscriptionId, :startDate, :endDate, :reason, :documentUrl)")
    void save(@BindMethods SubscriptionFreeze freeze);
}