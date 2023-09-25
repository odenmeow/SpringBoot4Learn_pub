package com.oni.training.springboot.MyProduct.repository;

import com.oni.training.springboot.MyProduct.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


// 必須是介面或抽象才能丟進去MongoRepository估計裡面用到代理反射
@Repository
public interface ProductRepository extends MongoRepository<Product,String> {

    List<Product> findByNameLike(String productName);
    List<Product> findByNameLikeIgnoreCase(String name, Sort sort);
    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom,int priceTo,String keyword,Sort sort);

//    因為以下是類似宣告field的事情 ，只是常量並無不可。
//    Sort sort = Sort.by(
//            Sort.Order.asc("price"),
//            Sort.Order.desc("name")
//    );
//  以下是文章提供者寫的供參考用 基本上就是@Query() 語法 這邊好像是原生

    // 查詢 price 欄位在特定範圍的文件（參數亦可使用 Date 資料）
    // gte：大於等於；lte：小於等於；Between：大於及小於，兩者略有差異。
    @Query("{'price': {'$gte': ?0, '$lte': ?1}}")
    List<Product> findByPriceBetween(int from, int to);

    // 查詢 name 字串欄位有包含參數的文件，不分大小寫
    @Query("{'name': {'$regex': ?0, '$options': 'i'}}")
    List<Product> findByNameLikeIgnoreCase(String name);
    // 查詢同時符合上述兩個條件的文件
    @Query("{'$and': [{'price': {'$gte': ?0, '$lte': ?1}}, {'name': {'$regex': ?2, '$options': 'i'}}]}")
    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom, int priceTo, String name);
    // 回傳 id 欄位值有包含在參數之中的文件數量
    @Query(value = "{'_id': {'$in': ?0}}", count = true)
    int countByIdIn(List<String> ids);

    // 回傳是否有文件的 id 欄位值包含在參數之中
    @Query(value = "{'_id': {'$in': ?0}}", exists = true)
    boolean existsByIdIn(List<String> ids);

    // 刪除 id 欄位值包含在參數之中的文件
    @Query(delete = true)
    void deleteByIdIn(List<String> ids);

    // 找出 id 欄位值有包含在參數之中的文件，且先以 name 欄位遞增排序，再以 price 欄位遞減排序
    @Query(sort = "{'name': 1, 'price': -1}")
    List<Product> findByIdInOrderByNameAscPriceDesc(List<String> ids);
}
