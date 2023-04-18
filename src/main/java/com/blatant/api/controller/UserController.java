package com.blatant.api.controller;

import com.blatant.api.dto.AdminUserResponse;
import com.blatant.api.dto.UserResponse;
import com.blatant.api.dto.UserSubscriptionResponse;
import com.blatant.api.entity.Subscription;
import com.blatant.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser(){
        try {
            UserResponse response = userService.getCurrnetUser();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception)
        {
            log.warn("Get user by auth error: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();

        }
    }

    @GetMapping("/subs")
    public ResponseEntity<?> getCurrentUserSubs(){
        try {
            List<Subscription> response =  userService.getCurrentUserSubs();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Get user subs error:{}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers(){
        try {
            List<AdminUserResponse> response = userService.getAllUsers();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Admin get all users: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
