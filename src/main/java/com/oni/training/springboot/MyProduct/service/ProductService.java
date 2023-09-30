package com.oni.training.springboot.MyProduct.service;

import com.oni.training.springboot.MyProduct.entity.SendMailRequest;
import com.oni.training.springboot.MyProduct.parameter.ProductQueryParameter;
import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import com.oni.training.springboot.MyProduct.entity.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.ProductResponse;
import com.oni.training.springboot.MyProduct.converter.ProductConverter;
import com.oni.training.springboot.MyProduct.entity.Product;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

// CH12移除了這邊的@Service跟建構子的@Autowired
// @DependsOn("InitialClearDB")  這不會有用 因為必須跟 @Bean @Component 其中一個搭配才行
// 測試看看到底有沒有push成功而已
public class ProductService  {
    private ProductRepository repository;
    // 方法被調用就寄信。
    private MailService mailService;
    // Configuration需要加入
    @Value("${app.data.initialize}")
    private boolean initializeData;
    // 這邊的Autowired也被移除了
    public ProductService(ProductRepository repository,MailService mailService) {
        this.mailService=mailService;
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
        System.out.println("@Value < initializeData >="+initializeData);
    }
    // 按照REST 觀念 GET POST PUT DEL 去放方法
    public Product getProduct(String id){
        return  repository.findById(id)
                .orElseThrow(()->new NotFoundException("Can't find product."));
    }
    public ProductResponse getProductResponse(String id){
        Product product= repository.findById(id)
                .orElseThrow(()->new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }
    // 回去舊版把它加進來了!原本CH11被替換，而不是多載，現在用成多載
    //  <舊版 CH11之前使用>
    public Product createProduct(Product reqeust){
        // 原本是因為id手動設定才需要 exception拋出 自訂義 (現在用不太到)
        Product product=new Product();
        product.setName(reqeust.getName());
        product.setPrice(reqeust.getPrice());

        return repository.insert(product);
    }
    // CH11新增的方法
    public Product createProduct(ProductRequest request){
        // 原本是因為id手動設定才需要 exception拋出 自訂義 (現在用不太到)

        Product product=ProductConverter.toProduct(request);
        return repository.insert(product);
    }
    // (CH12) 我這邊有大幅度修剪 名稱也略改 為了向下兼容
    public ProductResponse createProductRtJSON(ProductRequest request){
        Product product=ProductConverter.toProduct(request);
        repository.insert(product);
        return ProductConverter.toProductResponse(product);
    }


    // <舊版 CH11之前使用>
    public Product replaceProduct(String id,Product request){
        Product oldproduct=getProduct(id);
//      正常不能改的 不過這邊改給你看 (也就只是替換而已啦)
        Product product=new Product();
        product.setId(oldproduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);

    }
    // 使用方法多載，不然這樣之前的可能都不能使用了!
    // CH11新增的方法
    public Product replaceProduct(String id,ProductRequest request){
        Product oldproduct=getProduct(id);
        Product newProduct=ProductConverter.toProduct(request);
        newProduct.setId(oldproduct.getId());

        return repository.save(newProduct);

    }
    // CH12新增的方法
    public ProductResponse replaceProductRtJSON(String id,ProductRequest request) {
        Product oldproduct=getProduct(id);
        Product newproduct=ProductConverter.toProduct(request);
        newproduct.setId(oldproduct.getId());
        repository.save(newproduct);
        return ProductConverter.toProductResponse(newproduct);
    }




    public void deleteProduct(String id){
        getProduct(id);//多了這個如果不存在    【會說找不到】!
        repository.deleteById(id);
    }

    // CH11之前
    public List<Product> getProducts(ProductQueryParameter param){
        // 如果沒有關鍵字就用空字串替代了
        String keyword=Optional.ofNullable(param.getKeyword()).orElse("");
        Integer priceFrom=Optional.ofNullable(param.getPricefrom()).orElse(0);
        Integer priceTo=Optional.ofNullable(param.getPriceto()).orElse(Integer.MAX_VALUE);

        Sort sort=genSortingStrategy(param.getOrderBy(), param.getSortRule());

        System.out.println("keyword"+keyword+priceFrom+priceTo);


        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom,priceTo,keyword,sort);
    }
    // CH12
    public List<ProductResponse> getProductsRtJSON (ProductQueryParameter param){
        String keyword=Optional.ofNullable(param.getKeyword()).orElse("");
        Integer priceFrom=Optional.ofNullable(param.getPricefrom()).orElse(0);
        Integer priceTo=Optional.ofNullable(param.getPriceto()).orElse(Integer.MAX_VALUE);

        Sort sort=genSortingStrategy(param.getOrderBy(), param.getSortRule());
        System.out.println("keyword"+keyword+priceFrom+priceTo);
        List<Product> list= repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom,priceTo,keyword,sort);
        List<ProductResponse> jsonList=list.stream()
                                            .map(ProductConverter::toProductResponse)
                                            .collect(Collectors.toList());
        System.out.println("目前ProductService instance:"+this.hashCode());
        return jsonList;
    }
    private Sort genSortingStrategy(String orderby,String sortrule){
        Sort sort=Sort.unsorted();

        if(Objects.nonNull(orderby)&&Objects.nonNull(sortrule)){
            Sort.Direction direction=Sort.Direction.fromString(sortrule);
            sort=Sort.by(direction,orderby);
        }
        return sort;
    }
    public void mailNotify(SendMailRequest sendMailRequest) throws Exception {
        mailService.sendMail(sendMailRequest);

    }

}
