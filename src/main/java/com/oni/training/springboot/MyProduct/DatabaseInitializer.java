package com.oni.training.springboot.MyProduct;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("DatabaseInitializer")
@Order(1)
public class DatabaseInitializer implements ApplicationRunner {
    private final MongoTemplate mongoTemplate;
    private String targetDatabase="products";

    public DatabaseInitializer (MongoTemplate mongoTemplate){

        this.mongoTemplate=mongoTemplate;
        System.out.println("你會發現這個在建立資料庫之前，那你猜測成立");
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> databaseNames=mongoTemplate.getCollectionNames();
        System.out.println("我有在跑拉");
        if(databaseNames.contains(targetDatabase)){

            mongoTemplate.getDb().getCollection(targetDatabase).drop();
            System.out.println("成功刪除資料表");
        }else{
            System.out.println("Database " + targetDatabase + " not found, nothing to drop.");
        }

    }
}
