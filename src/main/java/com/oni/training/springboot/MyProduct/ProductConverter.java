package com.oni.training.springboot.MyProduct;

public class ProductConverter {
    private ProductConverter(){

    }
    public static Product toProduct(ProductRequest request){
        Product product=new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }
}
