package com.oni.training.springboot.MyProduct;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


// 必須是介面或抽象才能丟進去MongoRepository估計裡面用到代理反射
@Repository
public interface ProductRepository extends MongoRepository<Product,String> {

    List<Product> findByNameLike(String productName);
    List<Product> findByNameLikeIgnoreCase(String name, Sort sort);
    List<Product> findByPriceBetweenAndNameLikeIgnoreCase(Integer priceFrom,Integer priceTo,String keyword,Sort sort);

//    因為以下是類似宣告field的事情 ，只是常量並無不可。
//    Sort sort = Sort.by(
//            Sort.Order.asc("price"),
//            Sort.Order.desc("name")
//    );

}
