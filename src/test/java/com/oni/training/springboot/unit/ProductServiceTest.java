package com.oni.training.springboot.unit;

import com.oni.training.springboot.MyProduct.auth.auth_user.UserIdentity;
import com.oni.training.springboot.MyProduct.converter.ProductConverter;
import com.oni.training.springboot.MyProduct.entity.product.Product;
import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductResponse;
import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import com.oni.training.springboot.MyProduct.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public class ProductServiceTest {

    @Test
    public void testConvertProduct(){
        Product product =new Product();
        product.setId("123");
        product.setName("NOI");
        product.setPrice(55);
        product.setCreator("OniSan");
        ProductResponse productResponse= ProductConverter.toProductResponse(product);

        Assert.assertEquals(product.getId(),productResponse.getId());
        Assert.assertEquals(product.getName(),productResponse.getName());
        Assert.assertEquals(product.getPrice(),productResponse.getPrice());
        Assert.assertEquals(product.getCreator(),productResponse.getCreatorId());
    }

    @Test
    public void testGetProduct(){
        String productId="123";
        Product testProduct=new Product();
        testProduct.setId(productId);
        testProduct.setName("Onini");
        testProduct.setPrice(55);
        testProduct.setCreator("AAA");

        ProductRepository productRepository= Mockito.mock(ProductRepository.class);
        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.of(testProduct));
        ProductService productService=new ProductService(productRepository,null,null);
        // 下面getProduct內部使用了findById 所以會使用我們mock Repo的那個thenReturn == testProduct
        Product resultProduct=productService.getProduct(productId);
        Assert.assertEquals(testProduct.getId(),resultProduct.getId());
        Assert.assertEquals(testProduct.getName(),resultProduct.getName());
        Assert.assertEquals(testProduct.getPrice(),resultProduct.getPrice());
        Assert.assertEquals(testProduct.getCreator(),resultProduct.getCreator());
    }

    @Test
    public void testcreateProductRtJSON(){
        ProductRepository productRepository=Mockito.mock(ProductRepository.class);
        UserIdentity userIdentity=Mockito.mock(UserIdentity.class);

        String creatorId="Onini";
        Mockito.when(userIdentity.getId())
                .thenReturn(creatorId);
        ProductService productService=new ProductService(productRepository,null,userIdentity);
        ProductRequest productRequest=new ProductRequest();
        productRequest.setName("Onisan");
        productRequest.setPrice(30);
        productRequest.setId(userIdentity.getId());
        ProductResponse productResponse=productService.createProductRtJSON(productRequest);

        InOrder inOrder=Mockito.inOrder(productRepository,userIdentity);
        inOrder.verify(userIdentity,Mockito.times(1))
                .getId();
        inOrder.verify(productRepository,Mockito.times(1))
                .insert(Mockito.any(Product.class));

        Assert.assertEquals(productRequest.getName(),productResponse.getName());
        Assert.assertEquals(productRequest.getPrice().intValue(),productResponse.getPrice());
        Assert.assertEquals(creatorId,productResponse.getCreatorId());

    }





}
