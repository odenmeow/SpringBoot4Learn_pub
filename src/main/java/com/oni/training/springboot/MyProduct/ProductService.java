package com.oni.training.springboot.MyProduct;

import com.oni.training.springboot.WebExceptions.NotFoundException;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@DependsOn("InitialClearDB")
// 測試看看到底有沒有push成功而已
public class ProductService {
    private ProductRepository repository;

    @Value("${app.data.initialize}")
    private boolean initializeData;
    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository=repository;
    }

    @PostConstruct
    public void initDB(){
        repository.insert(new Product("Android Development (Java)", 380));
        repository.insert(new Product("Android Development (Kotlin)", 420));
        repository.insert(new Product("Data Structure (Java)", 250));
        repository.insert(new Product("Finance Management", 450));
        repository.insert(new Product( "Human Resource Management", 330));
        System.out.println("資料已經建立");
        // 啟動當下就看得到囉!
        System.out.println("順便讓你看一下properties 的 initializeData="+initializeData);
    }
    public Product createProduct(Product reqeust){
        // 原本是因為id手動設定才需要 exception拋出 自訂義 (現在用不太到)
        Product product=new Product();
        product.setName(reqeust.getName());
        product.setPrice(reqeust.getPrice());

        return repository.insert(product);
    }
    public Product getProduct(String id){
        return  repository.findById(id)
                .orElseThrow(()->new NotFoundException("Can't find product."));
    }
    public Product replaceProduct(String id,Product request){
        Product oldproduct=getProduct(id);
//      正常不能改的 不過這邊改給你看 (也就只是替換而已啦)
        Product product=new Product();
        product.setId(oldproduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);

    }

    public void deleteProduct(String id){
        getProduct(id);//多了這個如果不存在    【會說找不到】!
        repository.deleteById(id);
    }

    public List<Product> getProducts(ProductQueryParameter param){
        // 如果沒有關鍵字就用空字串替代了
        String keyword=Optional.ofNullable(param.getKeyword()).orElse("");
        Integer priceFrom=Optional.ofNullable(param.getPricefrom()).orElse(0);
        Integer priceTo=Optional.ofNullable(param.getPriceto()).orElse(Integer.MAX_VALUE);

        Sort sort=genSortingStrategy(param.getOrderBy(), param.getSortRule());

        System.out.println("keyword"+keyword+priceFrom+priceTo);


        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom,priceTo,keyword,sort);
    }
    private Sort genSortingStrategy(String orderby,String sortrule){
        Sort sort=Sort.unsorted();

        if(Objects.nonNull(orderby)&&Objects.nonNull(sortrule)){
            Sort.Direction direction=Sort.Direction.fromString(sortrule);
            sort=Sort.by(direction,orderby);
        }
        return sort;
    }

}
