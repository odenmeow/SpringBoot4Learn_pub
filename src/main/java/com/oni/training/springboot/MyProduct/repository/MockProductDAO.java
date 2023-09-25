package com.oni.training.springboot.MyProduct.repository;

//「mock」 這個詞通常用於類比或替代某些元件或物件，以用於測試或佔位元的目的。在軟體開發中，“mock” 通常表示虛擬的、類比的或替代的東西。

import com.oni.training.springboot.MyProduct.parameter.ProductQueryParameter;
import com.oni.training.springboot.MyProduct.entity.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MockProductDAO {

    private final List<Product> productDB = new ArrayList<>();


    @PostConstruct
    private  void initDB(){
        productDB.add(new Product("Android Development (Java)", 380));
        productDB.add(new Product( "Android Development (Kotlin)", 420));
        productDB.add(new Product( "Data Structure (Java)", 250));
        productDB.add(new Product( "Finance Management", 450));
        productDB.add(new Product( "Human Resource Management", 330));
        System.out.println("MOCK資料已經建立");
        // 啟動當下就看得到囉!
    }
    public Product insert(Product product){
        productDB.add(product);
        return product;
    }
    public Product replace (String id,Product product){
        Optional<Product> productOptional=find(id);
        productOptional.ifPresent(p->{

                p.setName(product.getName());
                p.setPrice(product.getPrice());
            }
        );
        return  product;
    }
    public void delete(String id){
        productDB.removeIf(p->p.getId().equals(id));
    }
    public Optional<Product> find(String id){
        return productDB.stream()
                .filter(p->p.getId().equals(id))
                .findFirst();
    }

    public List<Product> find(ProductQueryParameter param){

        String keyword= param.getKeyword();
        String orderBy=param.getOrderBy();
        String sortRule= param.getSortRule();
        System.out.println(keyword+":"+orderBy+":"+sortRule);
        Comparator<Product> comparator=genSortComparator(orderBy,sortRule);


        // keyword=>filter 、orderby+sortRule =>sorted(comparator)
        return productDB.stream()
                // 這邊忽略的話 有可能因為嚴格比較包含而導致找不到例如 java 找不到因為是Java
                .filter(p->p.getName().toUpperCase().contains(keyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());

    }

    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator=(p1,p2)->0;

        if(Objects.isNull(orderBy)||Objects.isNull(sortRule)){
            return  comparator;
        }
        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }
        return sortRule.equalsIgnoreCase("desc")
                ?comparator.reversed()
                :comparator;

    }


}
