package com.example.shopapp.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUntil {
    @Value("${jwt.expiration}")
    private long expiration;// lưu vào biến môi truường: enviroment

    @Value("${jwt.secretKey}")
    private String secretkey;
    public String generateToken(com.example.shopapp.models.User user){
        // properties = claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try{
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                    .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            // cos thể dùng logger
            System.err.println("cannot create jwt token, err: "+e.getMessage());
            return null;
        }
    }
    private Key getSignInkey(){
        byte[] bytes= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(bytes);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // check expiration: kiểm tra token hết han chưa
    public  boolean isTokenExpired(String token){
        Date expirationDate= this.extractClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }
}
