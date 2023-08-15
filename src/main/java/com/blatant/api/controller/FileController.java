package com.blatant.api.controller;

import com.blatant.api.dto.AddFileRequest;
import com.blatant.api.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {
    
    private final FileService fileService;
    Logger log = LoggerFactory.getLogger(FileController.class);
    
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @PostMapping( "/admin/add")
    public ResponseEntity<?> addFile(@RequestBody AddFileRequest request){
        try {
            fileService.addFile(request);
            log.info("Add file success: {}",request);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            log.error("Add file error:",e);
            return ResponseEntity.badRequest().build();
        }
    }
}
