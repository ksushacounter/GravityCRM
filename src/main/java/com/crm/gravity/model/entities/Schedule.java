package com.crm.gravity.model.entities;

import java.time.LocalTime;

public record Schedule(
        Long scheduleId,
        Long groupId,
        Long hallId,
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
