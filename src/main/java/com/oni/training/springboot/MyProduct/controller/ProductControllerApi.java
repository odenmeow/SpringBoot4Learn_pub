package com.oni.training.springboot.MyProduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oni.training.springboot.MyProduct.entity.mail.SendMailRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductQueryParameter;
import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Product" ,description = "Basic CRUD with Product")
public interface ProductControllerApi {
    // 按照 REST GET POST PUT DEL 放置，然後按先後盡量。
    @GetMapping("/hi")
    @Operation(summary = "sayHi" ,description = "Say Hi, [input name] To you")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successfully Reply")
    })
    String hi(@RequestParam(value = "myname") String name);

    // CH12 不同於 CH11 (Method Changed)
    @GetMapping("/{id}")
    @Operation(summary = "getProductById", description = "Literally Just Give Me Product ID",
            responses = {
                @ApiResponse(responseCode = "200",
                            description = "Get Product info successfully"),
                @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url",
                            content = @Content),
                @ApiResponse(responseCode = "404",
                            description = "Not found such a product" ,
                            content = @Content)
            }
    )
    ResponseEntity<ProductResponse> getProduct(@Parameter(description = "ID of product.") @PathVariable("id") String id);

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
    @GetMapping
    // 這邊ModelAttribute 傳入GET 其實也是解析 ?a="a"&b="b"而已
    @Operation(summary = "getByProductQueryParameter", description = "Give Me ProductQueryParameter Form.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Get Products info successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url.",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
                            description = "Not found such a product ,or wrong format." ,
                            content = @Content)
            }
    )
    ResponseEntity<?> getProducts(@Parameter(description = "should contains pricefrom") @Valid @ModelAttribute ProductQueryParameter param, Errors errors) throws JsonProcessingException;

    @PostMapping
        //在需要驗證Product傳入對象加上@Valid
    @Operation(summary = "insert a new Product", description = "If login(Authenticated Token) ,the product will contain the creatorID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Create Product successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url.",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
                            description = "Maybe a wrong format." ,
                            content = @Content)
            }
    )
    ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request, Errors errors);

    @PutMapping("/{id}")
    @Operation(summary = "Replace a Product's detail", description = "Should Provide ID in url and the same id in ProductRequest",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Replacement successful"),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url.",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
                            // 省略主詞 用分詞構句 兩邊都要ving
                            description = "Maybe having the wrong format ,or not providing the same id." ,
                            content = @Content)
            }
    )
    ResponseEntity<?> replaceProduct(
            @PathVariable("id") String id, @Parameter(description = "You should input same id as the url.") @Valid @RequestBody ProductRequest request, Errors errors);

    // Void表示不返回實例數據 僅返回狀態碼
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the Product", description = "Should Provide ID in url",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success"),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url.",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
//                            如果您確切知道某物體在任何情況下都不存在，則 "Is not existing" 更為合適。
//                            如果您想表達某物體在某一情況下不存在，則 "Is not exist" 可能更適合。
                            description = "Maybe not exist." ,
                            content = @Content)
            }
    )
    ResponseEntity<Void> deleteProduct(@PathVariable("id") String id);

    // 做一個寄信的功能 使用到product service內部的 mailService對象
    @DeleteMapping("/{id}/mail")
    @Operation(summary = "Delete the Product and Send the Email", description = "Should Provide ID in url and SendMailRequest in Body",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success"),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url.",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
//                            如果您確切知道某物體在任何情況下都不存在，則 "Is not existing" 更為合適。
//                            如果您想表達某物體在某一情況下不存在，則 "Is not exist" 可能更適合。
                            description = "Maybe no product or email exist." ,
                            content = @Content)
            }
    )
//                                      這是參數強制要求之類的 回憶一下   @RequestParam(name = "paramName", required = true)
    ResponseEntity<Void> deleteProduct(@PathVariable("id") String id,@Parameter(description = "Provide the email truly exist") @Valid @RequestBody SendMailRequest mailRequest) throws Exception;
}
