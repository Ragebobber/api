package com.blatant.api.controller;

import com.blatant.api.dto.SubscriptionEdditRequest;
import com.blatant.api.dto.SubscriptionRequest;
import com.blatant.api.dto.SubscriptionResponse;
import com.blatant.api.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/admin/active-sub/{id}")
    public ResponseEntity<?> activeSub(@PathVariable Long id){
        try {
            final SubscriptionResponse response = subscriptionService.activeSub(id);
            return  ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Active user sub error: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/admin/add-sub")
    public ResponseEntity<?> addSub(@RequestBody SubscriptionRequest request){
        try {
            final SubscriptionResponse response = subscriptionService.addSub(request);
            return  ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Add user sub error: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/admin/eddit-sub/{id}")
    public ResponseEntity<?> edditSub(@PathVariable Long id,@RequestBody SubscriptionEdditRequest request){
        try {
            final SubscriptionResponse response = subscriptionService.edditSub(id,request);
            return  ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Edit user sub error: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
