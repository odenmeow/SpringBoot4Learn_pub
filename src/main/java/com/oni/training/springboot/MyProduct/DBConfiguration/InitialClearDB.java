package com.oni.training.springboot.MyProduct.DBConfiguration;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("InitialClearDB")
@Order(1)
public class InitialClearDB {
    private final MongoTemplate mongoTemplate;
    private String targetDatabase="products";
    private Animal a;

    @Autowired
    // 依照Config的@Bean的方法名稱 去查找!反正必須要注入
    public InitialClearDB(MongoTemplate mongoTemplate,@Qualifier("monkey1")Animal a) {
        this.mongoTemplate = mongoTemplate;
        this.a=a;
    }
    @PostConstruct
    public void DBClean(){
        a.act();
        Set<String> databaseNames=mongoTemplate.getCollectionNames();
        System.out.println("準備刪除舊資料");
        if(databaseNames.contains(targetDatabase)){

            mongoTemplate.getDb().getCollection(targetDatabase).drop();
            System.out.println("成功刪除資料表");
        }else{
            System.out.println("Database " + targetDatabase + " not found, nothing to drop.");
        }
    }
}
