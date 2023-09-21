package com.oni.training.springboot.MyProduct.DBConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Human implements Animal{
    public void act(){
        System.out.println("癢癢的 ， 抓一下");
    }
}
