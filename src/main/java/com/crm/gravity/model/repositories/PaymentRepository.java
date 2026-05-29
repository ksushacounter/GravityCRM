package com.crm.gravity.model.repositories;

import com.crm.gravity.model.entities.Payment;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface PaymentRepository {

    @SqlQuery("SELECT payment_id, amount, payment_time, fiscal_sign, admin_id, sub_id, status FROM payments ORDER BY payment_id DESC")
    @RegisterConstructorMapper(Payment.class)
    List<Payment> findAll();
}
