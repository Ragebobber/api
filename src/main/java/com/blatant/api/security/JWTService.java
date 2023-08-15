package com.blatant.api.security;

import com.blatant.api.entity.User;
import com.blatant.api.exception.JWTParseTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JWTService {

    Logger log = LoggerFactory.getLogger(JWTService.class);
    @Value("${jwt.secret.access.key}")
    private  String jwtAccessSecret;

    @Value("${jwt.secret.access.time}")
    private  Integer jwtExpirationTime;

    public String generateToken(User user, HttpServletRequest request){
        final Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationTime).toInstant());
        final SecretKey key = Keys.hmacShaKeyFor((jwtAccessSecret+request.getRemoteAddr()).getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("BlatantWebApi")
                .setSubject(user.getLogin())
                .claim("role",user.getRole())
                .setExpiration(expirationDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateTokenClient(User user, HttpServletRequest request){
        final Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationTime).toInstant());
        final SecretKey key = Keys.hmacShaKeyFor((jwtAccessSecret + request.getRemoteAddr())
                .getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setIssuer("BlatantClientApi")
                .setSubject(user.getLogin())
                .claim("hwid",user.getHwid())
                .setExpiration(expirationDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(@NonNull String token,HttpServletRequest request) throws JWTParseTokenException{
        try {
            final SecretKey key = Keys.hmacShaKeyFor((jwtAccessSecret+request.getRemoteAddr()).getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e){
            throw new JWTParseTokenException(e.getMessage());
        }
    }

    public String getUserLogin(@NonNull String token,HttpServletRequest request) throws JWTParseTokenException {
        try {
            final SecretKey key = Keys.hmacShaKeyFor((jwtAccessSecret+request.getRemoteAddr()).getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        }
        catch (Exception e){
         throw new JWTParseTokenException(e.getMessage());
        }
    }
}
