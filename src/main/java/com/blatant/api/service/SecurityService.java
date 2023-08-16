package com.blatant.api.service;

import com.blatant.api.dto.SecurityInfoRequest;
import com.blatant.api.entity.BlockIpList;
import com.blatant.api.entity.User;
import com.blatant.api.entity.UserStatus;
import com.blatant.api.repository.BlockIpRespository;
import com.blatant.api.repository.UserRepository;
import com.blatant.api.security.user.UserSecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final BlockIpRespository blockIpRespository;
    Logger log = LoggerFactory.getLogger(SecurityService.class);
    
    public SecurityService(UserRepository userRepository, BlockIpRespository blockIpRespository) {
        this.userRepository = userRepository;
        this.blockIpRespository = blockIpRespository;
    }
    
    public void debugCheck(HttpServletRequest httpServletRequest) {
        BlockIpList blockIpList = blockIp(httpServletRequest);
        log.info("Ip was blocked due to debugging:{}", blockIpList);
        User user = blockUser();
        log.info("Block User due to debugging:{}", user);
    }
    
    public Boolean isBlockIp(String ip) {
        return blockIpRespository.findByIpAddress(ip).isPresent();
    }
    
    public void hashCheck(SecurityInfoRequest request, HttpServletRequest httpServletRequest) throws IOException, NoSuchAlgorithmException {
        File file = ResourceUtils.getFile("classpath:client/Blatant final.exe");
        final byte[] fileData = Files.readAllBytes(file.toPath());
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(fileData);
        String checksum = new BigInteger(1, encodedhash).toString(16);
        
        if (!checksum.equals(request.getHash())) {
            BlockIpList blockIpList = blockIp(httpServletRequest);
            User user = blockUser();
            log.info(
                    "Ip was  blocked due to Hash incorrect, local hash:{}, request hash:{}, blockIp:{}, User;{}",
                    checksum,
                    request.getHash(),
                    blockIpList,
                    user
            );
            
        }
    }
    
    private BlockIpList blockIp(HttpServletRequest httpServletRequest) {
        String ipAddres = httpServletRequest.getRemoteAddr();
        BlockIpList blockIpList = new BlockIpList();
        blockIpList.setIpAddress(ipAddres);
        blockIpRespository.save(blockIpList);
        return blockIpList;
    }
    
    private User blockUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            UserSecurityService userDetailsService = (UserSecurityService) authentication.getPrincipal();
            User user = userDetailsService.user();
            
            if (user.getStatus().equals(UserStatus.BLOCKED))
                return user;
            
            user.setStatus(UserStatus.BLOCKED);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
