package com.blatant.api.dto;

import com.blatant.api.entity.Subscription;
import com.blatant.api.entity.UserRole;
import com.blatant.api.entity.UserStatus;

import java.util.Date;
import java.util.List;

public class AdminUserResponse {

    private Long id;
    private String login;
    private Date dateOfRegistration;
    private String hwid;
    private UserStatus status;

    private UserRole role;
    private List<Subscription> userSubscription;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<Subscription> getUserSubscription() {
        return userSubscription;
    }

    public void setUserSubscription(List<Subscription> userSubscription) {
        this.userSubscription = userSubscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
