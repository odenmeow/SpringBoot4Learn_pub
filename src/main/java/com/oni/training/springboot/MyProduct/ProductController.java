package com.oni.training.springboot.MyProduct;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
// 視為 Controller+ResponseBody
// 回傳String的話   被視為HTML內容            Content-Type: text/plain;charset=UTF-8
// 回傳物件或者HashMap則換成 Json 方式回傳      Content-Type: application/json
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
// Content-Type   application/json  (那怕回傳是String)
// 似乎會讓方法都變成JSON表示  原本String 應該直接傳回字串吧?
// 讓所屬類別加上路徑
public class ProductController {
    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    private  void initDB(){
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
        System.out.println("資料已經建立");
        // 啟動當下就看得到囉!
    }
    @PreDestroy
    private void cleanUp(){
//        productDB=null;
    }
    @GetMapping("/hi")
    public String hi(@RequestParam(value = "myname") String name){
        return name;
    }
    @GetMapping("/products/order")
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param){

        String keyword= param.getKeyword();
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();
        System.out.println(keyword+":"+orderBy+":"+sortRule);
        Comparator<Product> comparator=genSortComparator(orderBy,sortRule);
        List<Product> products=productDB.stream()
                .filter(p->p.getName().toUpperCase().contains(keyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());
        return  ResponseEntity.ok().body(products);
    }
    private Comparator<Product> genSortComparator(String orderBy, String sortRule){
        Comparator<Product> comparator=(p1,p2)->0;
        if(Objects.isNull(orderBy)||Objects.isNull(sortRule)){
            System.out.println("提早返回");
            return comparator;
        }
        if(orderBy.equalsIgnoreCase("price")){
            comparator=Comparator.comparing(Product::getPrice);
        }else if(orderBy.equalsIgnoreCase("name")){
            comparator=Comparator.comparing(Product::getName);
        }
        return sortRule.equalsIgnoreCase("desc")?
                comparator.reversed():comparator;
    }





    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
           @RequestParam(value = "keyword",defaultValue = "") String name  ){

        List<Product> products=productDB.stream()
                .filter(p->p.getName().toUpperCase().contains(name.toUpperCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(products);

    }
    @GetMapping("/productsV2/{id}")
    public ResponseEntity<Product> getProductV2(@PathVariable("id") String id) {
        Optional<Product> productOptional=productDB.stream()
                .filter(p->p.getId().equals(id))
                .findAny();  //['a','b','c'] 隨機取用  但findfirst只找第一個符合的
        System.out.println("++++"+id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") String id){
        Product product = new Product();
        product.setId(id);
        product.setName("Romantic Story");
        product.setPrice(200);
        return product;
        /*{     "id": "1",
                "name": "Romantic Story",
                "price": 200           }*/
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product request){
        boolean isIdDuplicated = productDB.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));
        if (isIdDuplicated) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        //做出來了把路徑丟上去的感覺捏?
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(product);
//        201 created.
//        如果去看headers location會出現
//        localhost:8080/products?id=B0006&name=Enterprise Resource Planning&price=460
//        或
//        localhost:8080/products
//        取決於一開始傳送資料的路徑是否帶上請求參數 (但我function接收BODY JSON格式而已)。
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id,@RequestBody Product request){
        Optional<Product> productOptional=productDB.stream()
                .filter(p->p.getId().equals(id))
                .findFirst();
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            return ResponseEntity.ok().body(product);
        }else
        {
            return ResponseEntity.notFound().build();
        }

    }
    // Void表示不返回實例數據 僅返回狀態碼
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id){

        boolean isRemoved =productDB.removeIf(p->p.getId().equals(id));

        return isRemoved?
                ResponseEntity.noContent().build():
                ResponseEntity.notFound().build();
    }




}
