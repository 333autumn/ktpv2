package com.lsc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lsc.eneity.User;

/**
 * Token工具类
 */
public class TokenUtils {

    //token到期时间10小时
    private static final long EXPIRE_TIME= 10*60*60*1000;
    //密钥盐
    private static final String SALT="bushuohuazhuanglaoshifu";

    /**
     * 生成token
     */
    public static String generateToken(User user) {
        return JWT.create()
                .withClaim("userId", user.getUserId())
                .withClaim("expireAt", EXPIRE_TIME)
                .sign(Algorithm.HMAC256(SALT));
    }

    /**
     * 从token中获取用户id
     */
    public static String getUserId(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SALT)).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            return verify.getClaim("userId").asString();
        } catch (JWTVerificationException e) {
            return "";
        }

    }

}