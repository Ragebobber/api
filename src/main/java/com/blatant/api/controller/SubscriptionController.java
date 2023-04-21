package com.blatant.api.controller;

import com.blatant.api.dto.SubscriptionRequest;
import com.blatant.api.dto.SubscriptionResponse;
import com.blatant.api.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sub")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/admin/active-sub")
    public ResponseEntity<?> activeSub(@RequestBody SubscriptionRequest request){
        try {
            SubscriptionResponse response = subscriptionService.activeSub(request);
            return  ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Active user sub error: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
