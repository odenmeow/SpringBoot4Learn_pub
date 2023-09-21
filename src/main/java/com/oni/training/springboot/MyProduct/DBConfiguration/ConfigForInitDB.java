package com.oni.training.springboot.MyProduct.DBConfiguration;

import com.oni.training.springboot.MyProduct.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 這個在另外一份有說明，就是當啟動就會先跑這邊的樣子。有點類似Postcontruct
@Configuration
public class ConfigForInitDB {

    //這邊的bean 也會被使用 會被注入不是因為別人@Autowired了這個類別 而是因為他是配置類別
    @Bean
    public Animal monkey1(){
        return new Monkey();
    }
    // 載入可以載入 但是實際有沒有使用又是一回事 spring會需要才去找  那時候若沒Qualifier才會出事!
    @Bean
    public Animal h(){
        return new Human();
    }
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {

        };
    }
}
