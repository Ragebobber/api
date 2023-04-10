package com.blatant.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class Subscription {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "productId",nullable = false)
    private Product productId;

    @Temporal(TemporalType.DATE)
    Date expirationDate;

    Boolean isActive;

    protected Subscription() {
    }

    public Subscription(Long id, User userId, Product productId, Date expirationDate, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
