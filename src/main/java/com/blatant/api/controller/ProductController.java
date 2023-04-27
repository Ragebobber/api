package com.blatant.api.controller;


import com.blatant.api.dto.ProductRequest;
import com.blatant.api.dto.ProductResponse;
import com.blatant.api.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getProducts(){
        try {
            List<ProductResponse> response = productService.getAllProducts();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Get all products: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/admin/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request){
        try {
          ProductResponse response = productService.addProduct(request);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Add product: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/admin/active-product/{id}")
    public ResponseEntity<?> activeProduct(@PathVariable Long id){
        try {
            ProductResponse response = productService.activeProduct(id);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Active product: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/admin/eddit-product/{id}")
    public ResponseEntity<?> edditProduct(@PathVariable Long id,@RequestBody ProductRequest request){
        try {
            ProductResponse response = productService.edditProduct(id,request);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Active product: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        try {
            ProductResponse response = productService.deleteProduct(id);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception exception){
            log.warn("Delete product: {}",exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
