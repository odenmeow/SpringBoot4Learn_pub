package com.oni.training.springboot.MyProduct.DBConfiguration;

import com.oni.training.springboot.MyProduct.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 這個在另外一份有說明，就是當啟動就會先跑這邊的樣子。有點類似Postcontruct
@Configuration
public class ConfigForInitDB {
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {

        };
    }
}
