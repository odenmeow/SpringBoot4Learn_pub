package com.oni.training.springboot.MyProduct.auth;


import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY="09D7514AEB5C7B2501AEB004F2387F240C18350B3FD0A2EE988F8FF3C09A15FA";

    @Autowired
    private AppUserRepository appUserRepository;


    private Key getSignInKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken( Map<String,Object> extraClaims,
            UserDetails userDetails){
        /** 由於沒有辦法直接透過 參數設定 ， 使用repo來玩*/
        Optional<AppUser> Opuser= appUserRepository.findByEmailAddress(userDetails.getUsername());
        AppUser user= Opuser.orElseThrow(()->{throw new NotFoundException("找不到該使用者");});
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
//              最好別setId因為根本UserDetails 沒有提供 與要求 所以就先跳過比較安全不然就是另外提供參數進來
//                2023/10/21 11:54 笑死 我想說怎麼會通過 (RestTemplateTest 產品創建者跟透過Token取得user id
//                原來是因為兩個都是null ， 好的 ， 我準備來這邊真正設置看看
                .setId(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(token)
                .getBody();
    }
    public <R>R extractClaim(String token, Function<Claims,R> claimsTFunction){
        final Claims claims=extractAllClaims(token);  //通用格式Claims 轉換並記載 issued at expiration at 之類
        return claimsTFunction.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

}
