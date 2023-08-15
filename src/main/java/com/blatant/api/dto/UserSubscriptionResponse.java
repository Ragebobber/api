package com.blatant.api.dto;

import com.blatant.api.entity.Product;

import java.util.Date;

public class UserSubscriptionResponse {

    private Date expirationDate;

    private Boolean isActive;

    private Product productId;

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

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }
}
