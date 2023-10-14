package com.oni.training.springboot.MyProduct.converter;





///Onini_0316/src/at/com/home/extendOrinterface/testOfExtend.java
// 上面有做 如果父界面當抽換 ， 子界面重新定義 並不會影響到父界面
// 因此輸出還是舊的沒有使用到 @JSONnonNull的舊類別。
// 使用原因，因為@Entity 無法用介面/繼承玩抽換
// 使用轉換器 可以將 ResponseEntity<Product> 原本輸出改變一下
// 缺點: 需要修改蠻多代碼。

// 1.介面定義方法、抽象雖然可以屬性，但是兩者在當作ResponseEntity<父> 的話，回傳的都是[父].屬性而不是修改過的[子].屬性。
// 2.如果使用子類繼承抽象(介面沒屬性不討論)倒還好，雖然沒意義 回傳還是要改成 ResponseEntity<子>，但至少不會創建多個instance。
// 3.如果使用子類繼承父類，完，不只也要回傳改成 ResponseEntity<子>，還會創建(父+子)instance*2實例浪費空間。
// 4.抽象Product (1) > Product(2) >responseProduct(3) 這樣建立一堆子類 是可以驗證的，驗證將以input父類所設定的為主。所以沒用。
// 5.不要以為Qualifier可以注入，它只能幫你注入你修改過方法的類，透過泛型，可以方法覆寫，實現簡單的Decoupling。
// 6.@Valid只能在 Controller 或者自己手動開啟 ，具體在Test類的man.java 。
// 7.總之傳入的@Valid (類) 基本上 無法通過泛型去變換驗證的資格。

//=========================== 【坐而言不如起而行 ，重點如下】===============================


//@PostMapping("/example")
//  public ResponseEntity<String> example(@Valid @RequestBody Position p) {
//      不管input是"抽象"或"父類" @Valid 只會 驗證 Position 這個位置的類所定義的 @NotEmpty
//      它不會考慮父類的其他子類多型之驗證內容的事情。 驗證以父類寫的為主!
//      因為Json傳入並不知道怎麼對應類型 ，故無法自適應各種子類多態驗證。
//      回傳也一樣，回傳子類，取得也會只拿到以父類建構的JSON而已，但我們需要子類的格式。


import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductResponse;
import com.oni.training.springboot.MyProduct.entity.product.Product;

//================================================================
public class ProductConverter {
    private ProductConverter(){

    }
    public static Product toProduct(ProductRequest request){
        Product product=new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }
    // 新增:toProductResponse
    public static ProductResponse toProductResponse(Product product){
        ProductResponse response=new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setCreatorId(product.getCreator());
        return response;
    }
}
