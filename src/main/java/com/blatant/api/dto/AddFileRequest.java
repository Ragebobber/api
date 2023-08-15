package com.blatant.api.dto;

import java.util.Arrays;

public class AddFileRequest {
    
    private byte[] file;
    
    private Long productId;
    
    public byte[] getFile() {
        return file;
    }
    
    public void setFile(byte[] file) {
        this.file = file;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    @Override
    public String toString() {
        return "AddFileRequest{" +
                "file=" + Arrays.toString(file) +
                ", productId=" + productId +
                '}';
    }
}
