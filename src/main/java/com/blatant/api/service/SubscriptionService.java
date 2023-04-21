package com.blatant.api.service;

import com.blatant.api.dto.SubscriptionRequest;
import com.blatant.api.dto.SubscriptionResponse;
import com.blatant.api.entity.Subscription;
import com.blatant.api.exception.SubscriptionNotFound;
import com.blatant.api.repository.SubscriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper mapper;


    public SubscriptionService(SubscriptionRepository subscriptionRepository, ModelMapper mapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.mapper = mapper;
    }

    public SubscriptionResponse activeSub(SubscriptionRequest request) throws SubscriptionNotFound {
        Subscription subscription = subscriptionRepository.findById(request.getId()).orElseThrow(()-> new SubscriptionNotFound("No subs"));
        subscription.setActive(!subscription.getActive());
        subscriptionRepository.save(subscription);
        return mapper.map(subscription, SubscriptionResponse.class);
    }
}
