package com.blatant.api.dto;

import java.util.Date;

public class SubscriptionEdditRequest {
    private Boolean active;
    private Long id;
    private Date expirationDate;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "SubscriptionEdditRequest{" +
                "active=" + active +
                ", id=" + id +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
