package com.oni.training.springboot.MyProduct.Config;


import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import com.oni.training.springboot.MyProduct.service.MailService;
import com.oni.training.springboot.MyProduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ServiceConfig {


    @Bean
    @Autowired
    @DependsOn("InitialClearDB")  // 在 < 調用該方法創建 >之前 會先創建另一個bean
    public ProductService productService(ProductRepository repository,MailService mailService){
        System.out.println("Product Service is created.");
        return new ProductService(repository, mailService);
    }

}
