package com.oni.training.springboot.MyProduct.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;


public class LogProcessTimeFilter extends OncePerRequestFilter {

//    FilterChain 會將現有的 Filter 給串連起來，當請求進入後端，需要依序經過它們才會到達 Controller。
//    相對地，當回應離開 Controller，則是按照相反的順序經過那些 Filter。
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime=System.currentTimeMillis();
        filterChain.doFilter(request,response);
        long processTime=System.currentTimeMillis()-startTime;
        System.out.println("RequestProcessTime :"+processTime+" ms");
    }
}
