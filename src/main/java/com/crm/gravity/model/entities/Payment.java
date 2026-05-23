package com.crm.gravity.model.entities;

import java.time.LocalTime;

public record Payment(
        Long paymentId,
        int amount,
        LocalTime paymentTime,
        String fiscalSign,
        Long adminId,
        Long subId,
        String status
) {
}