package com.oni.training.springboot.MyProduct;

import com.oni.training.springboot.WebExceptions.NotFoundException;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private MockProductDAO productDAO;

    @Autowired
    public ProductService(MockProductDAO productDAO) {
        this.productDAO=productDAO;
    }

    public Product createProduct(Product reqeust){
        boolean isIdDuplicated=productDAO.find(reqeust.getId()).isPresent();

        if(isIdDuplicated){
            throw new UnprocessableEntityException("The id of the product is duplicated");
        }
        Product product=new Product();
        product.setId(reqeust.getId());
        product.setName(reqeust.getName());
        product.setPrice((reqeust.getPrice()));

        return productDAO.insert(product);
    }
    public Product getProduct(String id){
        return  productDAO.find(id)
                .orElseThrow(()->new NotFoundException("Can't find product."));
    }
    public Product replaceProduct(String id,Product request){
        Product product=getProduct(id);
//                                             如果使用id 則會導致輸出的商品被騙
        return productDAO.replace(id,request);

    }

    public void deleteProduct(String id){
        Product product=getProduct(id);
        productDAO.delete(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param){
        return productDAO.find(param);
    }

}
