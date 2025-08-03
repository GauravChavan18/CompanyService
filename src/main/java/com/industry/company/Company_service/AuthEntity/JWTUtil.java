package com.industry.company.Company_service.AuthEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username , List<String> roles, Long expiryMinutes) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000)) // 1 hour
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().
                parseClaimsJws(token).
                getBody();
    }

    public String getUserNameFromToken(String token) {

        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }
        catch (JwtException e)
        {
            return null;
        }
    }

    public Boolean isTokenExpired(String token)
    {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        String jwtusername =getUserNameFromToken(token);
        return (jwtusername != null && !isTokenExpired(token));
    }

    public String generateToken(String username, long expiryMinutes) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000)) // 1 hour
                .signWith(key)
                .compact();
    }
}
