package com.blatant.api.dto;

import com.blatant.api.entity.ProductStatus;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private ProductStatus status;
    
    public String getAltName() {
        return altName;
    }
    
    public void setAltName(String altName) {
        this.altName = altName;
    }
    
    private String altName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
