package com.oni.training.springboot.integration.MyProduct;


import com.beust.ah.A;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.product.Product;
import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.entity.product.ProductResponse;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.mapping.Language;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@RunWith(SpringRunner.class)  // 使用@Autowired 需要用到
// (GPT說如果AppBaseTest已經有SpringBootTest 就不需要這個@了)
// 註解看看 把AppBaseTest內的RunWith順便一起
public class RestTemplateTest extends AppBaseTest{

    @LocalServerPort
    private int severPort;

    private String domain;
    private RestTemplate restTemplate;

    @Before    //父類的這邊有一個名稱一樣的要改掉!
    public void init(){
        domain="http://localhost:"+severPort;
        restTemplate=new RestTemplate();
    }

    @Test
    public void testGetProduct(){
        Product product=createProduct("Umimi",1000);
        String url=domain+"/products/"+product.getId();

        //直接幫我做原本我需要使用ObjectMapper的事情了!
        ProductResponse productRes=restTemplate
                .getForObject(url,ProductResponse.class);

        Assert.assertNotNull(productRes);
        Assert.assertEquals(product.getId(),productRes.getId());
        Assert.assertEquals(product.getName(),productRes.getName());
        Assert.assertEquals(product.getPrice(), productRes.getPrice());

    }

    @Test
    public void testGetProducts() {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Financial Statement Analysis", 400);
        Product p4 = createProduct("Human Resource Management", 420);
        Product p5 = createProduct("Enterprise Resource Planning", 440);

        String url=domain+"/products?priceto={priceto}&pricefrom={pricefrom}&keyword={name}&orderBy={orderField}&sortRule={sortDirection}";

        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("name", "manage");
        queryParams.put("orderField", "price");
        queryParams.put("sortDirection", "asc");
//      下面使用 200跟420 會發現 原本ProductRepository 找尋的between居然不包含200、420
//      List<Product> findByPriceBetweenAndNameLikeIgnoreCase(int priceFrom,int priceTo,String keyword,Sort sort);
//      我嘗試使用Query 修改一下  並且舊的版本註解保留讓你參考
        queryParams.put("pricefrom","200");
        queryParams.put("priceto","420");

        // 不包含請求主體 所以Void 然後headers 也null
        HttpEntity<Void> httpEntity=new HttpEntity<>(null,null);

        ResponseEntity<List<ProductResponse>> responseEntity=
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<ProductResponse>>(){},
                        queryParams
                        );
        List<ProductResponse> productResponseList=responseEntity.getBody();
        Assert.assertNotNull(productResponseList);
        Assert.assertEquals(3,productResponseList.size());
        Assert.assertEquals(p2.getId(),productResponseList.get(0).getId());
        Assert.assertEquals(p1.getId(),productResponseList.get(1).getId());
        Assert.assertEquals(p4.getId(),productResponseList.get(2).getId());


    }
    @Test
    public void testUserAuthentication() throws Exception {
        AppUserRequest appUserRequest= AppUserRequest.builder()
                    .emailAddress("qw28425382694@gmail.com")
                    .password(USER_PASSWORD)
                    .name("onini666").build();


        AuthResponse authResponse=createUser(appUserRequest);
        // 下面參數不可以使用authResponse.getName() 因為會回傳 onini666 但是我需要的是emailAddress
        obtainAccessToken(appUserRequest.getEmailAddress());

    }


    @Test
    public void testCreateProduct() throws Exception {
        AppUserRequest appUserRequest= AppUserRequest.builder()
                .emailAddress("qw28425382694@gmail.com")
                .password(USER_PASSWORD)
                .name("onini666").build();
        AuthResponse authResponse=createUser(appUserRequest);


        ProductRequest productReq=ProductRequest.builder()
                .price(500)
                .name("Donut")
                .build();
        String token=obtainAccessToken(appUserRequest.getEmailAddress());
        httpHeaders.add(HttpHeaders.AUTHORIZATION,"Bearer "+token);

        HttpEntity<ProductRequest> httpEntity=
                new HttpEntity<>(productReq,httpHeaders);
        String url=domain+"/products";
        ProductResponse productRes=restTemplate
                .postForObject(url,httpEntity,ProductResponse.class);
        Assert.assertNotNull(productRes);
        Assert.assertEquals(productReq.getPrice().intValue(),productRes.getPrice());
        Assert.assertEquals(productReq.getName(),productRes.getName());
//        String id=jwtService.extractClaim(authResponse.getToken(), Claims::getId);
        String id=jwtService.extractClaim(authResponse.getToken(), c-> c.getId());
//         之所以能c->c.getId() 這樣是因為本來就 是傳入一個自定義lambda介面 然後由Function<T,R> claimsTFunction 幫忙做事
//         如果仔細去看會發現 claimsTFunction.apply(claims);  這邊就是幫忙傳入經過提取的Token == claims 到c然後c.getId()就會回傳
/**
 *          你如果有練習過  應該要知道 傳入參數 如果是 介面  那就要實作 介面有2種~3種  方式
 *          1. Claims::getId 方法映射       基本上相當於下面 2
 *          2. public <R>R extractClaim(String token, Function<Claims,R> claimsTFunction){
 *                  final Claims claims=extractAllClaims(token);  你方法傳入的token 被轉換後得到的claims
 *                  return claimsTFunction.apply(claims);
 *             }
 *              apply(claims)
 *                      |
 *                      |
 *                      一 一 一>  c->c.getId()    lambda  可以 快速 構建 匿名 方法  "名稱"為傳入介面 設定 這邊是 Function 自稱apply
 *              請擅自想像      claimsTFunction 是介面   使用介面 要替他 完成 介面  R apply(Claims c);
 *
 *
 *                          R  apply (Claim  c){
 *                              return c.getId
 *                          }
 *          3. c->{ 做一串運算之後 手動return value  ;}
 *
 *
*/


        Assert.assertEquals(id,productRes.getCreatorId());
        System.out.println(id);
        System.out.println(productRes.getCreatorId());

    }

    @Test
    public void testReplaceProduct() throws Exception {
        AppUserRequest appUserRequest= AppUserRequest.builder()
                .emailAddress("qw28425382694@gmail.com")
                .password(USER_PASSWORD)
                .name("onini666").build();
        AuthResponse authResponse=createUser(appUserRequest);

        Product product=createProduct("umi",10);
        ProductRequest productRequest=ProductRequest.builder()
//                service 還是 controller 有要求一定要
//                 1.輸入url有包含id
//                 2.ProductReq 有id   兩者還必須一樣不然會錯 {"errorMessage":"id必須兩邊相同"}
                .id(product.getId())
                .name("sweetHoney")
                .price(777).build();

        httpHeaders.add(HttpHeaders.AUTHORIZATION,"Bearer ");

        HttpEntity<ProductRequest> httpEntity =new HttpEntity<>(productRequest,httpHeaders);
        System.out.println("ID是:"+product.getId());
        String url=domain+"/products/"+product.getId();

        ResponseEntity<ProductResponse>  resp=restTemplate
                .exchange(
                        url,
                        HttpMethod.PUT,
                        httpEntity,
                        ProductResponse.class
                );
        ProductResponse productResponse=resp.getBody();
        Assert.assertNotNull(productResponse);
        Assert.assertEquals(productResponse.getName(),productRequest.getName());
        Assert.assertEquals(productResponse.getPrice(),productRequest.getPrice().intValue());

    }

//    @Test
//    public void testExchangeRateAPI(){
//
//        MappingJackson2HttpMessageConverter converter
//                =new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
//
//        restTemplate =new RestTemplateBuilder()
//                .setConnectTimeout(Duration.ofSeconds(10))
//                .additionalMessageConverters(converter)
//                .build();
//
//        String url="https://www.freeforexapi.com/api/live?pairs=USDTWD,USDEUR";
//        HttpEntity<Void> httpEntity=new HttpEntity<>(null,null);
//        ResponseEntity<ExchangeRateResponse> responseEntity=restTemplate
//                .exchange(
//                  url,
//                  HttpMethod.GET,
//                  httpEntity,
//                  new  ParameterizedTypeReference<ExchangeRateResponse>(){}
//                );
//
//        ExchangeRateResponse exRateRes=responseEntity.getBody();
//
//        Assert.assertNotNull(exRateRes);
//        Assert.assertNotNull(exRateRes.getRates().get("USDEUR"));
//        Assert.assertNotNull(exRateRes.getRates().get("USDTWD"));
//
//    }

    @Test
    public void testExchangeRateAPI(){

        MappingJackson2HttpMessageConverter converter
                =new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
//      依照需求 看 回應會是 TEXT 還是 JSON   原作者提供的那個網址 header 為 HTML  Pokemon header JSON所以要改一下
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));

        
        restTemplate =new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .additionalMessageConverters(converter)
                .build();

        String url="https://pokeapi.co/api/v2/version/9/";
        HttpEntity<Void> httpEntity=new HttpEntity<>(null,null);
        ResponseEntity<pokemonStatusResponse> responseEntity=restTemplate
                .exchange(
                  url,
                  HttpMethod.GET,
                  httpEntity,
                  new  ParameterizedTypeReference<pokemonStatusResponse>(){}
                );

        pokemonStatusResponse pokemonRes=responseEntity.getBody();

        Assert.assertNotNull(pokemonRes);
        Assert.assertNotNull(pokemonRes.getId());
        Assert.assertEquals(pokemonRes.getName(),"emerald");

    }






    private Product createProduct(String name,int price){

        Product product=new Product();
        product.setName(name);
        product.setPrice(price);
        return productRepository.insert(product);
    }

    private String obtainAccessToken(String username){
        AuthRequest authRequest=new AuthRequest();
        authRequest.setEmail(username);
        authRequest.setPassword(USER_PASSWORD);

        String url=domain+"/users/authenticate";

        // 回傳是body
        Map tokenRes=restTemplate
                .postForObject(url,authRequest,Map.class);

        Assert.assertNotNull(tokenRes);
        String token=(String) tokenRes.get("token");
        Assert.assertNotNull(token);
        return token;
    }

    @Data
    public static class pokemonStatusResponse{

        private int id;
        private String name;
        private List<NameData> names;
        private VersionGroup version_group;
    }
    @Data
    public static class VersionGroup {
        private String name;
        private String url;
    }
    @Data
    public static class NameData{
        private langua language;
        private String name;
    }
    @Data
    public static class langua{
        private String name;
        private String url;
    }

    // 用不了( 網址被閹割，需要創帳號了，不想創帳號，我用pokemon api來玩
    public static class ExchangeRateResponse{

        private  Map<String,RateData> rates;
        private int code;
        public Map<String,RateData> getRates(){
            return rates;
        }
        public void  setRates(Map<String,RateData> rates){
            this.rates=rates;
        }
        public int getCode(){
            return code;
        }
        public void setCode(int code){
            this.code=code;
        }

    }
    @Data
    public static class RateData{
        private double rate;
        private long timestampe;
        public double getRate(){
            return rate;
        }

    }

























}
