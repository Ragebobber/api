package com.blatant.api.service;

import com.blatant.api.dto.ProductRequest;
import com.blatant.api.dto.ProductResponse;
import com.blatant.api.entity.Product;
import com.blatant.api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper mapper;


    public ProductService(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(elem -> mapper.map(elem, ProductResponse.class)).toList();
    }

    public ProductResponse addProduct(@NonNull ProductRequest request){
        Product savedProduct = mapper.map(request,Product.class);
        productRepository.save(savedProduct);
        return mapper.map(savedProduct, ProductResponse.class);
    }
}
