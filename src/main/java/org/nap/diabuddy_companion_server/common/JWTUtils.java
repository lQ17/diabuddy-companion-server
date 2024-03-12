package org.nap.diabuddy_companion_server.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static String TOKEN = "token!NaP1nIne0ZerO";
    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,1);
        builder.withExpiresAt(instance.getTime());//设置一个月过期
        return builder.sign(Algorithm.HMAC256(TOKEN));
    }
    /**
     * 验证token
     * @param
     * @return
     */
    public static void verify(String token){
        String tokenToVerify = token.substring(7);
        JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(tokenToVerify);
    }
    /**
     * 获取token中payload 使用require()
     * @param token
     * @return
     */
//    public static DecodedJWT getToken(String token){
//        return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
//    }
}

