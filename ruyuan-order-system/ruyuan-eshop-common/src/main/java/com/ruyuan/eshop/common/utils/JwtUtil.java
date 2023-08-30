package com.ruyuan.eshop.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * <p>
 * JWT工具类
 * </p>
 *
 * @author pwd
 * @version 1.0
 */
public class JwtUtil {

    // 以下配置，在生产环境应该做成可配置的 TODO

    /**
     * 过期时间，一个月
     */
    private static final long EXPIRE = 60000 * 60 * 24 * 30L;
//    private  static final long EXPIRE = 10 * 60 * 1000;


    /**
     * 加密秘钥
     */
    private static final String SECRET = "wADxWwW1YW7OTNdq";


    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "ruyuan";


    /**
     * subject
     */
    private static final String SUBJECT = "ruyuan";


    /**
     * 根据用户信息，生成令牌
     *
     * @param userAccountId 账号ID
     * @param username      用户名
     * @return
     */
    public static String geneJsonWebToken(Long userAccountId, String username) {

        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", userAccountId)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

        token = TOKEN_PREFIX + token;


        return token;
    }


    /**
     * 校验token的方法
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {

        try {

            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

            return claims;

        } catch (Exception e) {
            return null;
        }

    }


}