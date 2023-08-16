package com.blatant.api.controller;

import com.blatant.api.dto.SecurityInfoRequest;
import com.blatant.api.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    
    private final SecurityService securityService;
    Logger log = LoggerFactory.getLogger(SecurityController.class);
    
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    @PostMapping("/debug")
    public ResponseEntity<?> debugCheck(HttpServletRequest httpServletRequest){
        try {
            securityService.debugCheck(httpServletRequest);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/info")
    public ResponseEntity<?> getInfo(@RequestBody SecurityInfoRequest request,HttpServletRequest httpServletRequest){
        try {
            securityService.hashCheck(request,httpServletRequest);
            return ResponseEntity.ok("0x13");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
