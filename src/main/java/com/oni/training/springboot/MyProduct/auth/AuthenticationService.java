package com.oni.training.springboot.MyProduct.auth;


import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.*;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.ConflictException;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(AppUserRequest appUserRequest){
//        UserAuthority.ADMIN.fromString("admin");
        Optional<AppUser> existingUser=repository.findByEmailAddress(appUserRequest.getEmailAddress());
        existingUser.ifPresent((s)->{
            throw new ConflictException("和現有資料庫衝突"+s.getEmailAddress()+"已存在");
        });
        AppUser user= AppUser.builder()
                .name(appUserRequest.getName())
                .emailAddress(appUserRequest.getEmailAddress())
                .password(passwordEncoder.encode(appUserRequest.getPassword()))
                .role(Stream
                        .of(Role.NORMAL)
                        .collect(Collectors.toList()))   // 這建成的 才可變
                .build();
        // Stream.of(Role.NORMAL).toList()   這是跟 List.of() 一樣不可變更的表 如果試圖remove add會拋錯誤
        // 或者使用 ArrayList (但沒有builder所以也不太適合塞裡面)
        // 或     Arrays.asList(new Role[]{Role.ADMIN});

        repository.save(user);
        var jwtToken=jwtService.generateToken(new SpringUser(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request){

        //  SpringSecurity不鼓勵自己寫驗證方法 建議用這
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
        );
        var user=repository.findByEmailAddress(request.getEmail()).orElseThrow();
        SpringUser springUser=new SpringUser(user);
        var jwtToken=jwtService.generateToken(springUser);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }




}
