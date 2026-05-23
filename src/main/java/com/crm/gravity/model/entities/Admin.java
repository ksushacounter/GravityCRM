package com.crm.gravity.model.entities;


public record Admin(
        Long AdminId,
        String fio,
        String phone,
        String access_level
) {
}