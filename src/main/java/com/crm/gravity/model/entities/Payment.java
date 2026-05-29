package com.crm.gravity.model.entities;

import java.time.LocalDateTime;

public record Payment(
        Long paymentId,
        int amount,
        LocalDateTime paymentTime,
        String fiscalSign,
        Long adminId,
        Long subId,
        String status
) {
}
