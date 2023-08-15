package com.blatant.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User userId;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product productId;

    @Temporal(TemporalType.DATE)
    Date expirationDate;

    Boolean isActive;

    public Subscription() {
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
    @JsonIgnore
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

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", expirationDate=" + expirationDate +
                ", isActive=" + isActive +
                '}';
    }
}
