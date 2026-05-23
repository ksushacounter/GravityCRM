package com.crm.gravity.model.entities;

public record Student(
        Long studentId,
        String fio,
        String phone,
        String status // ACTIVE, DEBTOR
) {
}