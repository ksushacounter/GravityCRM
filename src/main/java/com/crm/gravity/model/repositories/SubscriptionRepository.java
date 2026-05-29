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
import java.util.List;


public interface SubscriptionRepository {

    @SqlQuery("SELECT type_id, name, price, visit_count, sub_kind FROM sub_types WHERE type_id = :typeId")
    @RegisterConstructorMapper(SubTypes.class)
    SubTypes findTypeById(@Bind("typeId") Long typeId);

    @SqlQuery("SELECT type_id, name, price, visit_count, sub_kind FROM sub_types ORDER BY type_id")
    @RegisterConstructorMapper(SubTypes.class)
    List<SubTypes> findAllTypes();

    @SqlUpdate("INSERT INTO subscriptions (sub_number, student_id, type_id, end_date, status, visit_count) " +
            "VALUES (:subNumber, :studentId, :typeId, :endDate, :status, :visitCount)")
    @GetGeneratedKeys("sub_id")
    Long createSubscription(@BindMethods Subscriptions subscription);

    @SqlUpdate("INSERT INTO payments (amount, fiscal_sign, sub_id, status) " +
            "VALUES (:amount, :fiscalSign, :subId, :status)")
    void createPayment(@Bind("amount") int amount,
                       @Bind("fiscalSign") String fiscalSign,
                       @Bind("subId") Long subId,
                       @Bind("status") String status);

    @SqlQuery("SELECT sub_id, sub_number, student_id, type_id, purchase_date, end_date, status, visit_count FROM subscriptions WHERE sub_id = :subId")
    @RegisterConstructorMapper(Subscriptions.class)
    Subscriptions findById(@Bind("subId") Long subId);

    @SqlQuery("SELECT sub_id, sub_number, student_id, type_id, purchase_date, end_date, status, visit_count FROM subscriptions ORDER BY sub_id DESC")
    @RegisterConstructorMapper(Subscriptions.class)
    List<Subscriptions> findAll();

    @SqlUpdate("UPDATE subscriptions SET end_date = :endDate WHERE sub_id = :subId")
    void updateEndDate(@Bind("subId") Long subId, @Bind("endDate") LocalDate endDate);
}
