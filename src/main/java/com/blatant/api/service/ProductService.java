package com.blatant.api.service;

import com.blatant.api.dto.ProductRequest;
import com.blatant.api.dto.ProductResponse;
import com.blatant.api.entity.Product;
import com.blatant.api.entity.ProductStatus;
import com.blatant.api.exception.ProductNotFound;
import com.blatant.api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    @Transactional
    public ProductResponse addProduct(@NonNull ProductRequest request){
        Product savedProduct = mapper.map(request,Product.class);
        productRepository.save(savedProduct);
        return mapper.map(savedProduct, ProductResponse.class);
    }
    @Transactional
    public ProductResponse activeProduct(@NonNull Long id) throws ProductNotFound {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()->
                        new ProductNotFound("Product not found!"));
        findProduct.setStatus(findProduct.getStatus().equals(ProductStatus.ACTIVE) ? ProductStatus.DISABLE : ProductStatus.ACTIVE);
        productRepository.save(findProduct);
        return mapper.map(findProduct, ProductResponse.class);
    }
    @Transactional
    public ProductResponse edditProduct(Long id,@NonNull ProductRequest request) throws ProductNotFound {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()->
                        new ProductNotFound("Product not found!"));
        findProduct.setName(request.getName());
        findProduct.setDescription(request.getDescription());
        productRepository.save(findProduct);
        return mapper.map(findProduct, ProductResponse.class);
    }
    @Transactional
    public ProductResponse deleteProduct(Long id) throws ProductNotFound {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()->
                        new ProductNotFound("Product not found!"));
        productRepository.delete(findProduct);
        return mapper.map(findProduct, ProductResponse.class);
    }

}
