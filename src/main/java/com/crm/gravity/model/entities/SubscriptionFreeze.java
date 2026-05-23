package com.crm.gravity.model.entities;

import java.time.LocalDate;

public record SubscriptionFreeze(
        Long freezeId,
        Long clientSubscriptionId,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        String documentUrl,
        LocalDate createdAt
) {
}