package com.blatant.api.service;

import com.blatant.api.dto.AddFileRequest;
import com.blatant.api.entity.Product;
import com.blatant.api.exception.ProductNotFound;
import com.blatant.api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


@Service
public class FileService {
    private final ProductRepository productRepository;
    public FileService(ProductRepository productRepository, ObjectMapper mapper) {
        this.productRepository = productRepository;
    }
    
    public void addFile(AddFileRequest request) throws ProductNotFound, IOException {
       Product product = productRepository.findById(request.getProductId()).orElseThrow(ProductNotFound::new);
       
       if(Objects.isNull(request.getFile()) || request.getFile().length == 0)
           throw  new IllegalArgumentException("Incorrect File");
       
       Path path = Paths.get( product.getName() + ".dll");
       Files.write(path, request.getFile());
    }
}
