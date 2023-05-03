package com.blatant.api.service;

import com.blatant.api.dto.SubscriptionRequest;
import com.blatant.api.dto.SubscriptionResponse;
import com.blatant.api.entity.Product;
import com.blatant.api.entity.Subscription;
import com.blatant.api.entity.User;
import com.blatant.api.exception.AddUserSubscriptionException;
import com.blatant.api.exception.ProductNotFound;
import com.blatant.api.exception.SubscriptionNotFound;
import com.blatant.api.repository.ProductRepository;
import com.blatant.api.repository.SubscriptionRepository;
import com.blatant.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    public SubscriptionService(SubscriptionRepository subscriptionRepository, ModelMapper mapper, UserRepository userRepository, ProductRepository productRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    public SubscriptionResponse activeSub(Long id) throws SubscriptionNotFound {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(()-> new SubscriptionNotFound("No subs"));
        subscription.setActive(!subscription.getActive());
        subscriptionRepository.save(subscription);
        return mapper.map(subscription, SubscriptionResponse.class);
    }
    @Transactional
    public SubscriptionResponse addSub(@NonNull SubscriptionRequest request) throws ProductNotFound, AddUserSubscriptionException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        final boolean checkSub = user.getUserSubscription()
                .stream()
                .anyMatch(elem -> elem.getProductId()
                        .getId()
                        .equals(request.getProductId()));

        if(checkSub)
            throw  new AddUserSubscriptionException("Old subscription is found!");

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ProductNotFound("Product not found!"));

        Subscription subscription = new Subscription();

        subscription.setUserId(user);
        subscription.setProductId(product);
        subscription.setExpirationDate(request.getExpirationDate());
        subscription.setActive(false);

        subscriptionRepository.save(subscription);
        return mapper.map(subscription,SubscriptionResponse.class);
    }

    @Scheduled(fixedDelayString = "PT12H")//every 12 hours
    @Transactional
    public void deactiveSubByExpirationDate() {
        try {
            List<Subscription> subscriptions = subscriptionRepository.findAllByExpirationDateBeforeAndIsActive(new Date(),true);

            if(subscriptions.isEmpty())
            {
                log.info("Deactivate subs by ex. date is empty: {}, date: {} ",subscriptions,new Date());
                return;
            }
           // Deactivate Subs List
            subscriptions = subscriptions.stream()
                    .peek(subscription -> subscription.setActive(false))
                    .toList();

            subscriptionRepository.saveAll(subscriptions);

            log.info("Deactivate subs: {}, date: {} ",subscriptions,new Date());

        }catch (Exception exception)
        {
            log.error("Deactivate subs error: ",exception);
        }
    }

}
