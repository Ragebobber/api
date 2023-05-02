package com.blatant.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @OneToMany(mappedBy = "productId",cascade = CascadeType.REMOVE)
    private List<Subscription> productSubscription = new ArrayList<>();

    public Product(Long id, String name, String description, ProductStatus status,List<Subscription> productSubscription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.productSubscription = productSubscription;
    }

    protected Product() {
    }

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
    @JsonIgnore
    public List<Subscription> getProductSubscription() {
        return productSubscription;
    }

    public void setProductSubscription(List<Subscription> productSubscription) {
        this.productSubscription = productSubscription;
    }
}
