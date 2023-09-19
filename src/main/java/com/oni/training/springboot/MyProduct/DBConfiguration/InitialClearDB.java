package com.oni.training.springboot.MyProduct.DBConfiguration;


import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("InitialClearDB")
@Order(1)
public class InitialClearDB {
    private final MongoTemplate mongoTemplate;
    private String targetDatabase="products";

    public InitialClearDB(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @PostConstruct
    public void DBClean(){
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
