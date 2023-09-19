package com.oni.training.springboot.MyProduct;


import org.springframework.data.mongodb.core.mapping.Document;

// MySQL+PostgresSQL 可以用@Entity (搭著JPA用)
// Document 則是NoSQL使用

//@Document(collation = "products")   'Field 'locale' is invalid in: { locale: "products" }'   因為是下面才正確

@Document(collection ="products" )
public class Product {
    /**
     * Java 類別中若有名稱為「id」的欄位，都將被轉換成「_id」。
     * 程式讀取資料時，也會轉換回來。
     * 至於「_class」欄位，則是告訴函式庫要將文件轉換回哪一種 Java 物件。
     * */

    private String id;
    private String name;
    private Integer price;
    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Product() {

    }


}
