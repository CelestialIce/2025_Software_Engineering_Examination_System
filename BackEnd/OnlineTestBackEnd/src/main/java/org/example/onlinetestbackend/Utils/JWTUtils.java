package org.example.onlinetestbackend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWTUtils {
    private static String KEY="GenshinImpact";
    private static Long expire=3600*1000L;

    public static String generateToken(Map<String, Object> claims){
        return  Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512,KEY)
                .setExpiration(new Date(System.currentTimeMillis()+expire))
                .compact();
    }

    public static Claims parseToken(String token){
        return  Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
