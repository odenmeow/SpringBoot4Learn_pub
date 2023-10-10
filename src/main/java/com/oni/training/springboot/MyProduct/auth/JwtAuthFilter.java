package com.oni.training.springboot.MyProduct.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            System.out.println("請求未夾帶門票，無須浪費時間試圖驗證");
            filterChain.doFilter(request,response);
            // 直接讓他進入下一站( Maybe 要註冊帳號 ， 也不須驗 )
            return ; //記得要return否則 filter回來的時候 會往下 就會出錯喔
        }
        jwt=authHeader.substring(7);
        System.out.println("夾帶的Token為: "+jwt);
        userEmail=jwtService.extractUsername(jwt);
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //若email有東西，且 (   ) ?
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);

            //下面這邊提供token 又提供userDetails是為了userDetails能從repo驗證是否一致
            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request) );

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
            filterChain.doFilter(request,response);
        }

    }
}
