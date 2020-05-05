package com.xlcxx.utils;

import com.xlcxx.plodes.system.domain.MyUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {


    private static final String SIGIN_SALUT = "xlxcc";

    private static final long EXPIRE_TIME = 3600 * 4 * 2000;

    /**
     * 用户登录成功后生成Jwt
     */
    public static String createJWT(MyUser user) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String, Object> claims = new HashMap();
        claims.put("username", user.getUsername());
        //生成签发人
        String subject = user.getUsername();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, SIGIN_SALUT);
        long expMillis = nowMillis + EXPIRE_TIME;
        Date exp = new Date(expMillis);
        //设置过期时间
        builder.setExpiration(exp);
        return builder.compact();
    }

    /**
     * Token的解密
     */
    public static Claims parseJWT(String token) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(SIGIN_SALUT)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     * 校验token  主要校验时间
     */
    public static Boolean isVerify(String token, MyUser user) {
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(SIGIN_SALUT)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        Date date = claims.getExpiration();
        Date niw = new Date();
        if (date.compareTo(niw) < 0) {
            return false;
        }
        return true;
    }
}