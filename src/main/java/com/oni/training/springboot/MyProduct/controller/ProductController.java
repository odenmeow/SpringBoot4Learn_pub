package com.oni.training.springboot.MyProduct.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.oni.training.springboot.MyProduct.entity.*;
import com.oni.training.springboot.MyProduct.parameter.ProductQueryParameter;
import com.oni.training.springboot.MyProduct.service.ProductService;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
// 視為 Controller+ResponseBody
// 回傳String的話   被視為HTML內容            Content-Type: text/plain;charset=UTF-8
// 回傳物件或者HashMap則換成 Json 方式回傳      Content-Type: application/json
@RequestMapping(value="/products", produces = MediaType.APPLICATION_JSON_VALUE)
// Content-Type   application/json  (那怕回傳是String)
// 似乎會讓方法都變成JSON表示  原本String 應該直接傳回字串吧?
// 讓所屬類別加上路徑
// docker連線測試的時候使用mongosh mongodb://localhost:27017 -u aaa -p ccc
public class ProductController {
    private ProductService productService;


    // 被調用的原因是 實作類或者@Bean回傳的ProductService 只有ServiceConfig的那一個方法
    @Autowired
    public ProductController(ProductService productService) {
        this.productService=productService;
    }

    // 按照 REST GET POST PUT DEL 放置，然後按先後盡量。
    @GetMapping("/hi")
    public String hi(@RequestParam(value = "myname") String name){
        return name;
    }

    // CH12 不同於 CH11 (Method Changed)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id){
        ProductResponse product=productService.getProductResponse(id);
        return ResponseEntity.ok(product);
        /*{     "id": "1",
                "name": "Romantic Story",
                "price": 200           }*/
    }

    // CH12 不同於 CH11 (Method Changed)
//    @GetMapping  // 這邊ModelAttribute 傳入GET 其實也是解析 ?a="a"&b="b"而已
//    public ResponseEntity<List<ProductResponse>> getProducts(@Valid @ModelAttribute ProductQueryParameter param){
//
//        System.out.println("進來了(ProductQueryParameter)");
//        List<ProductResponse> products= productService.getProductsRtJSON(param);
//        System.out.println("準備出去");
////        return  ResponseEntity.ok().body(products);
//        return  ResponseEntity.ok(products);  //等效上方
//    }
    // CH12-2  改變消息 更精細 fine-grained
    // postman localhost:8080/products?orderBy=price&keyword=&sortRule=desc&pricefrom=&priceto=
    @GetMapping  // 這邊ModelAttribute 傳入GET 其實也是解析 ?a="a"&b="b"而已
    public ResponseEntity<?> getProducts( @Valid @ModelAttribute ProductQueryParameter param,Errors errors) throws JsonProcessingException {
        if(errors.hasErrors()){
            Map<String, String> map = new HashMap<>();
            List<FieldError> fieldErrors = errors.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            // JSONObject 轉換格式為toString
            ObjectMapper objectMapper=new ObjectMapper();
//            如果先寫下面這個 然後放進去 response 會 無法正確顯示  而且Map<String, Object> response  必須Obj
//            var mapToJSON=objectMapper.writeValueAsString(map);
//            System.out.println(mapToJSON);
            // 由Spring?ValidateExceptionHanlder建立的內容 自己建立
            String path = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
            /**              treeMap 升序而非插入順序 要用LinkedHashMap   (我取消使用了因為不方便模組化)             */
            CustomBadResponse response=new CustomBadResponse(map,path);
//          下面是轉為JSON格式 但是因為我用REST 而且我requestMapping有設定JSON了
            var finalResponse=objectMapper.writeValueAsString(response);
//            不要裡面包裹字串 會亂喔 反正多玩幾次!
            return ResponseEntity.badRequest().body(response);
            //          return ResponseEntity.badRequest().build(); 沒有body只有空蕩蕩的400 BadRequest.
        }else {
            System.out.println("進來了(ProductQueryParameter)");
            List<ProductResponse> products = productService.getProductsRtJSON(param);
            products=productService.getProductsRtJSON(param);
//                    >> proxyMode=Target.Class  如果調用兩次會多產生一個ProductService出來
//                   .  >>  this.hashcode 表示 這兩個方法所使用的productService 不同! 雖然引用的Repo是同一個
            System.out.println("準備出去");
            return ResponseEntity.ok(products);
        }
    }

//    public ResponseEntity<> sendEmail(@Valid  @RequestBody EmailPostBody emailPostBody,
//                                  Errors errors) {
//    if(errors.hasErrors()) {
//        new ResponseEntity<>(youResponseBodyWithErrorMsg, httpStatusCode)
//    } https://stackoverflow.com/questions/63239623/how-to-customize-the-response-of-failed-validation-when-using-valid-in-springbo


//    @PostMapping                     //在需要驗證Product傳入對象加上@Valid
//    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
//        // RequestBody傳入Product Raw JSON
//        //{
//        //    "name": "Data Structure (Java)",
//        //    "price": 250
//        //}  是真的可以新增哦
//        System.out.println("request未建立id項目所以為:"+request.getId()); //null 而已
//        ProductResponse product=productService.createProductRtJSON(request);
//        //做出來了把路徑丟上去的感覺捏?
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(product.getId())
//                .toUri();
//        System.out.println("創建好之後有id了為:"+product.getId());
//        return ResponseEntity.created(location).body(product);
////        201 created.
////        如果去看headers location會出現
////        localhost:8080/products?id=B0006&name=Enterprise Resource Planning&price=460
////        或
////        localhost:8080/products
////        取決於一開始傳送資料的路徑是否帶上請求參數 (但我function接收BODY JSON格式而已)。
//    }

    @PostMapping                     //在需要驗證Product傳入對象加上@Valid
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request,Errors errors){
        if(errors.hasErrors()) {
            HashMap<String,String> map=new HashMap<>();
            List<FieldError> fieldErrors=errors.getFieldErrors();
            for(var err:fieldErrors){
                map.put(err.getField(),err.getDefaultMessage());
            }
            String path = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
            CustomBadResponse response=new CustomBadResponse(map,path);
            return ResponseEntity.badRequest().body(response);

        }else {
            // RequestBody傳入Product Raw JSON
            System.out.println("request未建立id項目所以為:" + request.getId()); //null 而已
            ProductResponse product = productService.createProductRtJSON(request);
            //做出來了把路徑丟上去的感覺捏?
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(product.getId())
                    .toUri();
            System.out.println("創建好之後有id了為:" + product.getId());
            return ResponseEntity.created(location).body(product);
        }
    }







    @PutMapping("/{id}")
    public ResponseEntity<?> replaceProduct(
            @PathVariable("id") String id,@Valid @RequestBody ProductRequest request,Errors errors){
        if(errors.hasErrors()){
            HashMap<String,String> map =new HashMap<>();
            for( var err:errors.getFieldErrors()){
                map.put(err.getField(),err.getDefaultMessage());
            }
            String path=ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
            CustomBadResponse response =new CustomBadResponse(map,path);
            return ResponseEntity.badRequest().body(response);
        }else {
            if (Objects.isNull(id) || Objects.isNull(request.getId())) {
                throw new UnprocessableEntityException("不可不寫上id");
            } else {
                if (id.equals(request.getId())) {
                } else {
                    throw new UnprocessableEntityException("id必須兩邊相同");
                }
            }
//      上面不管了，我把後面強制以url要改的為主，如果request偷改 id則無效!
            ProductResponse product = productService.replaceProductRtJSON(id, request);
            return ResponseEntity.ok(product);
        }
    }
    // Void表示不返回實例數據 僅返回狀態碼
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id){

       productService.deleteProduct(id);
       return ResponseEntity.noContent().build();
    }

    /** @Note 請注意雖然這邊測試沒跳出錯誤 但是 其實MailService 有setPortListener 會佔據port 如果有人還沒離開就觸發程式 那會出bug*/
    // 做一個寄信的功能 使用到product service內部的 mailService對象
    @DeleteMapping("/{id}/mail")
//                                      這是參數強制要求之類的 回憶一下   @RequestParam(name = "paramName", required = true)
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id ,@Valid @RequestBody SendMailRequest mailRequest) throws Exception {

        productService.deleteProduct(id);
        productService.mailNotify(mailRequest);
        return ResponseEntity.noContent().build();
    }




}
