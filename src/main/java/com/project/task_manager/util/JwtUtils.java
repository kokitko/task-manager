package com.project.task_manager.util;

import com.project.task_manager.entity.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Autowired
    private SecurityConstants securityConstants;

    public String generateToken(String username, Role role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+securityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, securityConstants.KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(securityConstants.KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(securityConstants.KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(securityConstants.KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
