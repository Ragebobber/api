package com.blatant.api.service;

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

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final BlockIpRespository blockIpRespository;
    Logger log = LoggerFactory.getLogger(SecurityService.class);
    
    public SecurityService(UserRepository userRepository, BlockIpRespository blockIpRespository) {
        this.userRepository = userRepository;
        this.blockIpRespository = blockIpRespository;
    }
    
    public void debugCheck(HttpServletRequest httpServletRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        String ipAddres = httpServletRequest.getRemoteAddr();
        BlockIpList blockIpList = new BlockIpList();
        blockIpList.setIpAddress(ipAddres);
        blockIpRespository.save(blockIpList);
        
        log.info("Ip was blocked due to debugging:{}",blockIpList);
        
        if(!authentication.getPrincipal().equals("anonymousUser")){
            UserSecurityService userDetailsService = (UserSecurityService) authentication.getPrincipal();
            User user = userDetailsService.user();
            
            if(user.getStatus().equals(UserStatus.BLOCKED))
                return;
            
            user.setStatus(UserStatus.BLOCKED);
            userRepository.save(user);
            log.info("Block User due to debugging:{}",user);
        }
    }
    
    public Boolean isBlockIp(String ip){
        return blockIpRespository.findByIpAddress(ip).isPresent();
    }
}
