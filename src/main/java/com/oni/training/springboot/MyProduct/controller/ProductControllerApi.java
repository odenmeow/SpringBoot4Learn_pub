package com.oni.training.springboot.MyProduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oni.training.springboot.MyProduct.entity.mail.SendMailRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductQueryParameter;
import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "getProductById which you just inserted is the best.", description =
            "Literally Just Give Me Product ID. Each time restart the sever, the product id will be changed." +"<br>"+
            "注意，id 最好使用自己剛剛新增的商品(最好先去新增再來這查)，才會出現creator id，" + "<br>"+
            " 否則需要重新查詢bash db 的商品id， 因為每次開啟伺服器都會清空、生成。",
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
    ResponseEntity<ProductResponse> getProduct(
            // 下面不可以使用 name ，否則不會自動轉化 pathvariable {id} ，會導致錯誤。
//           @Parameter(in = ParameterIn.PATH,name = "ID of product.", description = "productID", example = "65d39af607373933193f6212")
           @Parameter( description = "productID", example = "65d39af607373933193f6212")
           @PathVariable("id") String id);

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
                            description = "Get Products info successfully",
                            content =@Content(array = @ArraySchema(schema = @Schema(implementation = String.class)) ,
                            examples ={
                                    // 陣列[]一定要給 不能只給 {} , {} 會壞掉
                                    // 沒有填入value 也沒關係，語法糖，預設直接輸入是對應value
                                    @ExampleObject(

                                            """
                                                            [
                                                               {
                                                                 "id": "65d45fe765bd6233636ec4ce",
                                                                 "name": "Android Development (Java)",
                                                                 "price": 380,
                                                                 "creatorId": null
                                                               },
                                                               {
                                                                 "id": "65d45fe765bd6233636ec4d0",
                                                                 "name": "Data Structure (Java)",
                                                                 "price": 250,
                                                                 "creatorId": null
                                                               }
                                                             ]
                                                    """

                                    ) ,
                            })


                    ),
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
                    @ApiResponse(responseCode = "201",
                            description = "Create Product successfully",
                            content = @Content(mediaType = "application/json",
                                    examples = {@ExampleObject(value =
                                            """
                                                        {
                                                                  "id": "65d440fcafce1d56df946b30",
                                                                  "name": "Android Development (JavaScript)",
                                                                  "price": 205
                                                                }        
                                                    """),

                                    })
                    ),
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
                        description = "Replacement successful",
                        content = @Content(mediaType = "application/json",
                                examples = {@ExampleObject(value =
                                        """
                                                    {
                                                              "id": "65d440fcafce1d56df946b30",
                                                              "name": "Android Development (JavaScript)",
                                                              "price": 205
                                                            }        
                                                """),

                                }),
                            headers = {
                                    @Header(name = "Cache-Control", description = "no-cache,no-store,max-age=0,must-revalidate"),
                                    @Header(name = "Connection", description = "keep-alive"),
                                    @Header(name = "Content-Length", description = "104"),
                                    @Header(name = "Content-Type", description = "application/json"),
                                    @Header(name = "Date", description = "Tue,20 Feb 2024 06:45:30 GMT"),
                                    @Header(name = "Expires", description = "0"),
                                    @Header(name = "Keep-Alive", description = "timeout=60"),
                                    @Header(name = "Pragma", description = "no-cache"),
                                    @Header(name = "X-Content-Type-Options", description = "nosniff"),
                                    @Header(name = "X-Frame-Options", description = "DENY"),
                                    @Header(name = "X-XSS-Protection", description = "0"),
                            }
                    ),
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
            @Parameter( description = "id as the pathVariable of url .", example = "65d440fcafce1d56df946b30")
            @PathVariable("id") String id,  @Valid @RequestBody ProductRequest request, Errors errors);

    // Void表示不返回實例數據 僅返回狀態碼
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the Product", description = "Should Provide ID in url",
            responses = {
                    @ApiResponse(responseCode = "204",
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
