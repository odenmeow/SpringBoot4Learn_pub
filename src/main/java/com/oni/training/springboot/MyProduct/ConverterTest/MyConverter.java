package com.oni.training.springboot.MyProduct.ConverterTest;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Configuration 用不到先註解
public class MyConverter {

    @Bean
    public ChildOrigin childOrigin(){
        return new ChildOrigin();
    }
    @Bean
    public Child_2 child_2(){
        return new Child_2();
    }
}
