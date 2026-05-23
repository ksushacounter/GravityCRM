package com.crm.gravity.model.entities;

public record SubTypes(
        Long typeId,
        String name,
        int price,
        Integer visitCount,
        String subKind
) {
}
