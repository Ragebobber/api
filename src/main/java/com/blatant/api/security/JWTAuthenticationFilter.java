package com.blatant.api.security;

import com.blatant.api.entity.User;
import com.blatant.api.security.user.UserDetailsServiceImpl;
import com.blatant.api.security.user.UserSecurityService;
import com.blatant.api.service.SecurityService;
import com.blatant.api.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    
    private final SecurityService securityService;

    Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(JWTService jwtService, UserDetailsServiceImpl userDetailsService, UserService userService, SecurityService securityService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.securityService = securityService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String agent = request.getHeader(HttpHeaders.USER_AGENT);
        final boolean isClient = Objects.nonNull(agent) && agent.contains("Boost.Beast");
        final boolean isBlockIp = securityService.isBlockIp(request.getRemoteAddr());

        if( isBlockIp)
            return;
        
        if(Objects.isNull(header) || !header.startsWith("Bearer ") ){
            filterChain.doFilter(request,response);
            return;
        }

        final String token = header.substring(7);
        try {

            if(isClient)
            {
                Claims jwtClaims = jwtService.getClaims(token,request);

                String userLogin = jwtClaims.getSubject();
                String userHwid = jwtClaims.get("hwid",String.class);

                UserSecurityService userSecurityService = (UserSecurityService) userDetailsService.loadUserByUsername(userLogin);
                User curUser = userSecurityService.user();
                

                if(curUser.getHwid().equals(userHwid)){

                    UserDetails userDetails = new UserSecurityService(curUser);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
            else{
                String login = jwtService.getUserLogin(token,request);

                UserDetails userDetails = userDetailsService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception e){
            log.warn("Filter jwt auth error: {}, token:{}",e.getMessage(),token);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request,response);
    }
}
