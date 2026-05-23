package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.SubTypes;
import com.crm.gravity.model.entities.Subscriptions;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;


public interface SubscriptionRepository {

    @SqlQuery("SELECT type_id, name, price, visit_count, sub_kind FROM sub_types WHERE type_id = :typeId")
    @RegisterConstructorMapper(SubTypes.class)
    SubTypes findTypeById(@Bind("typeId") Long typeId);

    @SqlUpdate("INSERT INTO subscriptions (sub_number, student_id, type_id, end_date, status) " +
            "VALUES (:subNumber, :studentId, :typeId, :endDate, :status)")
    @GetGeneratedKeys("sub_id")
    Long createSubscription(@BindMethods Subscriptions subscription);

    @SqlUpdate("INSERT INTO payments (amount, fiscal_sign, sub_id, status) " +
            "VALUES (:amount, :fiscalSign, :subId, :status)")
    void createPayment(@Bind("amount") int amount,
                       @Bind("fiscalSign") String fiscalSign,
                       @Bind("subId") Long subId,
                       @Bind("status") String status);

    @SqlQuery("SELECT sub_id, sub_number, student_id, type_id, end_date, status FROM subscriptions WHERE sub_id = :subId")
    @RegisterConstructorMapper(Subscriptions.class)
    Subscriptions findById(@Bind("subId") Long subId);

    @SqlUpdate("UPDATE subscriptions SET end_date = :endDate WHERE sub_id = :subId")
    void updateEndDate(@Bind("subId") Long subId, @Bind("endDate") LocalDate endDate);
}