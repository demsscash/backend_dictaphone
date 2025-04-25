package com.personnel.personnelservice.security;

import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.core.ports.services.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private final SecretKey key;
    private final String masterKey;
    private final RoleService roleService;
    @Value("${app.token.expiration}")
    private long tokenExpiration;
    @Value("${app.token.remember-me.expiration}")
    private long rememberMeExpiration;
    @Value("${app.token.reset-password.expiration}")
    private long resetPasswordExpiration;

    public JwtTokenUtil(@Value("${app.secret.key}") String masterKey, RoleService roleService){
        this.masterKey=masterKey;
        this.roleService = roleService;
        byte[] keyBytes= Base64.getDecoder().decode(masterKey.getBytes(StandardCharsets.UTF_8));
        this.key=new SecretKeySpec(keyBytes,"HmacSHA256");
    }
    public String generateToken(User userDetails, boolean rememberMe ) {
        return generateToken(userDetails, rememberMe,false);
    }

    public String generateToken(User userDetails, boolean rememberMe , boolean isPatient) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if(isPatient){
            authorities = roleService.getRoleByName("PATIENT").getPermissions().stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        long expirationTime = rememberMe ? rememberMeExpiration : tokenExpiration;
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", authorities);
        claims.put("id", userDetails.getId());
        claims.put("email", userDetails.getEmail());
        claims.put("isPatient", isPatient);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String generateResetPasswordToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + resetPasswordExpiration))
                .signWith(key)
                .compact();
    }
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}
