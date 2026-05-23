package com.crm.gravity.model.entities;

import java.time.LocalDate;

public record Attedance(
        Long visitId,
        Long subId,
        Long scheduleId,
        LocalDate visitDate,
        Boolean isExtra,
        Boolean isSingle
) {
}
