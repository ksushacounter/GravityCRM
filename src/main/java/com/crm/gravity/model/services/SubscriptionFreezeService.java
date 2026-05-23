package com.crm.gravity.model.services;

import com.crm.gravity.dto.FreezeRequest;
import com.crm.gravity.model.entities.SubscriptionFreeze;
import com.crm.gravity.model.entities.Subscriptions;
import com.crm.gravity.model.repositories.SubscriptionFreezeRepository;
import com.crm.gravity.model.repositories.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class SubscriptionFreezeService {

    @Autowired
    private SubscriptionFreezeRepository freezeRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void freezeSubscription(Long subscriptionId, FreezeRequest request, String documentPath) {
        Subscriptions subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription == null) {
            throw new EntityNotFoundException("Абонемент не найден с ID: " + subscriptionId);
        }

        long freezeDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        LocalDate newEndDate = subscription.endDate().plusDays(freezeDays);
        subscriptionRepository.updateEndDate(subscriptionId, newEndDate);

        SubscriptionFreeze freeze = new SubscriptionFreeze(
                null,
                subscriptionId,
                request.getStartDate(),
                request.getEndDate(),
                request.getReason(),
                documentPath,
                null
        );

        freezeRepository.save(freeze);
    }
}