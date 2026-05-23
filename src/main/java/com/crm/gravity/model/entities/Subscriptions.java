package com.crm.gravity.model.entities;

import java.time.LocalDate;

public record Subscriptions(
        Long subId,
        String subNumber,
        Long studentId,
        Long typeId,
        LocalDate purchaseDate,
        LocalDate endDate,
        SubscriptionStatus status
) {
}