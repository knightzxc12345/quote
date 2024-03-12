package com.quote.utils;

import com.quote.base.Common;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secret}")
    private String secret;

    private static Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public static String extractUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // 取得使用者名稱
    public static String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 取得指定Claim參數
    public static <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    // 產生Token
    public static String generateToken(final String userUuid) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userUuid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Common.JWT_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // 驗證Token是否過期
    public static boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // 取得所有Claim
    private static Claims extractAllClaim(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 取得Token過期時間
    private static Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

}