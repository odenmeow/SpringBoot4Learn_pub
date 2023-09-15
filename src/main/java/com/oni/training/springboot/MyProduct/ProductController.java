package com.oni.training.springboot.MyProduct;


import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value="/products", produces = MediaType.APPLICATION_JSON_VALUE)
// Content-Type   application/json  (那怕回傳是String)
// 似乎會讓方法都變成JSON表示  原本String 應該直接傳回字串吧?
// 讓所屬類別加上路徑
public class ProductController {
    private ProductService productService;



    @Autowired
    public ProductController(ProductService productService) {
        this.productService=productService;
    }


    @GetMapping("/hi")
    public String hi(@RequestParam(value = "myname") String name){
        return name;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter param){
        List<Product> products= productService.getProducts(param);

//        return  ResponseEntity.ok().body(products);
        return  ResponseEntity.ok(products);  //等效上方
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){
        Product product=productService.getProduct(id);
        return ResponseEntity.ok(product);
        /*{     "id": "1",
                "name": "Romantic Story",
                "price": 200           }*/
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request){

        Product product=productService.createProduct(request);
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
    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id,@RequestBody Product request){
        if(Objects.isNull(id)||Objects.isNull(request.getId())){
            throw new UnprocessableEntityException("不可不寫上id");
        }else{
            if(id.equals(request.getId())){}else {
                throw new UnprocessableEntityException("id必須兩邊相同");
            }
        }
//      上面不管了，我把後面強制以url要改的為主，如果request偷改 id則無效!
        Product product =productService.replaceProduct(id,request);
        return ResponseEntity.ok(product);

    }
    // Void表示不返回實例數據 僅返回狀態碼
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id){

       productService.deleteProduct(id);
       return ResponseEntity.noContent().build();
    }




}
