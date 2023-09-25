package com.oni.training.springboot.MyProduct.entity;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    /**想知道有那些 滑鼠對 @NotEmpty懸浮 */
    /**點選 套件名稱 */
    // 不可以是null或者空字串
    // 自訂訊息! 發生錯誤才會回傳內容 (application.properties要設定才有效)
    @NotEmpty  (message = "Product name is undefined.")
    private String name;
    @NotNull
    @Min(value = 0,message = "Price should be greater or equal to 0.")
    // 寫完後要去controller寫@Valid  安裝在put 跟post
    // 改成小寫比較好
    private int price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product() {

    }


}
