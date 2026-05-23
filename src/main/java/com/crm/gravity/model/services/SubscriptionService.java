package com.crm.gravity.model.services;

import com.crm.gravity.model.entities.SubTypes;
import com.crm.gravity.model.entities.SubscriptionStatus;
import com.crm.gravity.model.entities.Subscriptions;
import com.crm.gravity.model.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SubscriptionService {

    private static final String MOCK_FISCAL_SIGN = "STUB_FISCAL_SIGN_WITHOUT_KASSA_2026";

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public Long issueSubscription(Long studentId, Long typeId) {
        SubTypes type = subscriptionRepository.findTypeById(typeId);
        if (type == null) {
            throw new IllegalArgumentException("Тип абонемента с ID " + typeId + " не найден!");
        }

        String subNumber = "SUB-" + System.currentTimeMillis();

        LocalDate endDate = LocalDate.now().plusDays(30);

        Subscriptions newSub = new Subscriptions(
                null,
                subNumber,
                studentId,
                typeId,
                null,
                endDate,
                SubscriptionStatus.ACTIVE
        );
        Long subId = subscriptionRepository.createSubscription(newSub);

        subscriptionRepository.createPayment(
                type.price(),
                MOCK_FISCAL_SIGN,
                subId,
                "COMPLETED"
        );

        return subId;
    }
}