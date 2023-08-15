package com.blatant.api.service;

import com.blatant.api.dto.SubscriptionEdditRequest;
import com.blatant.api.dto.SubscriptionLoadResponse;
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
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final UserService userService;
    Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    public SubscriptionService(SubscriptionRepository subscriptionRepository, ModelMapper mapper, UserRepository userRepository, ProductRepository productRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userService = userService;
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

    @Transactional
    public SubscriptionResponse edditSub(Long id,@NonNull SubscriptionEdditRequest request) throws SubscriptionNotFound {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(()-> new SubscriptionNotFound("No subs"));
        if(request.getExpirationDate() != null){
            if (request.getExpirationDate().after(new Date())){
                subscription.setExpirationDate(request.getExpirationDate());
            }
            else{
                log.warn("Add new Date to Sub error: new date < date now: new Date:{},request:{}",request.getExpirationDate(),request);
                throw new SubscriptionNotFound("New Date error");
            }
        }
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

    public SubscriptionLoadResponse loadSub(Long id) throws IOException {
        User currentUser = userService.getUserSecurity().user();
        List<Subscription> userSubscription = currentUser.getUserSubscription();

        Subscription loadedSubscription = userSubscription.stream()
                .filter(elem -> (elem.getId().equals(id) && elem.getActive()))
                .findFirst()
                .orElse(null);


        SubscriptionLoadResponse response = new SubscriptionLoadResponse();
        if(Objects.isNull(loadedSubscription))
            return null;
        File file = new File(loadedSubscription.getProductId().getName() + ".dll");
        final byte[] fileBytes = Files.readAllBytes(file.toPath());
        response.setData(Base64.encodeBase64String(fileBytes));
        return response;

    }

}
