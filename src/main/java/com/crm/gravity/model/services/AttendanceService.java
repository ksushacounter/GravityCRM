package com.crm.gravity.model.services;

import com.crm.gravity.model.repositories.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public void recordAttendance(Long subId, Long scheduleId) {
        if (!attendanceRepository.isSubscriptionValid(subId)) {
            throw new IllegalStateException("Абонемент недействителен или на нем закончились занятия!");
        }

        attendanceRepository.markVisit(subId, scheduleId);
    }
}