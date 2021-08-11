package com.fintech.insurance.commons.utils;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * JWT 帮助类
 */
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * 生成jwt token
     * @param headers
     * @param claims
     * @param expireDate
     * @param issuer
     * @param signKey
     * @return
     */
    public static String encode(Map<String, String> headers, Map<String, String> claims, Date expireDate, String issuer, String signKey) {
        JwtBuilder jwtBuilder = Jwts.builder();
        //设置头
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey())) {
                    jwtBuilder.setHeaderParam(entry.getKey(), entry.getValue());
                }
            }
        }
        //设置claims
        if (claims != null && claims.size() > 0) {
            for (Map.Entry<String, String> entry : claims.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey())) {
                    jwtBuilder.claim(entry.getKey(), entry.getValue());
                }
            }
        }
        //
        if (expireDate != null) {
            jwtBuilder.setExpiration(expireDate);
        }
        if (StringUtils.isNotEmpty(issuer)) {
            jwtBuilder.setIssuer(issuer);
        }
        jwtBuilder.setIssuedAt(new Date());
        return jwtBuilder.signWith(SignatureAlgorithm.HS256, signKey).compact();
    }

    /**
     * 解析jwt token
     * @param token
     * @param signKey
     * @return
     */
    public static Jws<Claims> decode(String token, String signKey) {
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> jwt = null;
        try {
            jwt = jwtParser.setSigningKey(signKey).parseClaimsJws(token);
        } catch (Exception e) {
            logger.error("Fail to parse the jwt token", e);
        }
        return jwt;
    }
}
