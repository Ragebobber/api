package com.blatant.api.controller;

import com.blatant.api.dto.AuthenticationResponse;
import com.blatant.api.dto.LoginRequest;
import com.blatant.api.dto.RegisterRequest;
import com.blatant.api.dto.ResponseStatus;
import com.blatant.api.entity.User;
import com.blatant.api.security.JWTService;
import com.blatant.api.security.user.UserDetailsServiceImpl;
import com.blatant.api.security.user.UserSecurityService;
import com.blatant.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(),request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserSecurityService userDetailsService = (UserSecurityService) authentication.getPrincipal();

            AuthenticationResponse response = new AuthenticationResponse();

            response.setAccessToken(jwtService.generateToken(userDetailsService.getUser(),httpServletRequest));

           log.info("User is authorized, login: {}, ip:{}",userDetailsService.getUser().getLogin(),httpServletRequest.getRemoteAddr());

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            log.warn("Login warning:{}",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/v1/registration")
    public ResponseEntity<?> registration( @RequestBody RegisterRequest request){
        try {
            User user =  userService.registerUser(request);
           log.info("Success registration: {}",user);
           return  ResponseEntity.ok().body(ResponseStatus.SUCCESS);
        }
        catch (Exception e){
            log.warn("Login registration:{}",e.getMessage());
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @GetMapping("/v1/check")
    public ResponseEntity<AuthenticationResponse> checkJWT(HttpServletRequest httpServletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityService userDetailsService = (UserSecurityService) authentication.getPrincipal();
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken(jwtService.generateToken(userDetailsService.getUser(), httpServletRequest));
        return ResponseEntity.ok().body(response);
    }

}
