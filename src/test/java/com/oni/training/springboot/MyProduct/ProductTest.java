package com.oni.training.springboot.MyProduct;


import com.oni.training.springboot.MyProduct.entity.Product;
import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 因為專案使用Spring框架所以要使用Spring提供的這個
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    // 使用之前請先建立ProductT專用的資料庫跟products喔 避免混亂(雖然重啟都是重設拉哈哈) 練練看

    private  HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    //似乎是使用【反射+繼承】來訪問 需要public才能做
    //Field privateField = MyClass.class.getDeclaredField("privateField");
    //privateField.setAccessible(true);  預設不能接觸private!
    //因為有測試，需要外部類能夠訪問才能執行操作：JUnit 本身沒有直接使用代理。
    @Before
    public void ini(){
        httpHeaders =new HttpHeaders(); //初始化物件
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
    }
    @After
    @Before  //前後都先清乾淨(因為我預先會載入幾筆資料!)
    public void clear(){
        productRepository.deleteAll(); //清空資料庫
    }

    // 下面是第一個寫的方法
    @Test
    public void testCreateProduct() throws Exception{
        //選擇springFramework提供的版本
//        下面兩個都被我隱藏了 因為我們有用init()初始化加入了
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        JSONObject request=new JSONObject()
                .put("name","OOoni")
                .put("price",455);

        RequestBuilder requestBuilder=
                MockMvcRequestBuilders
                        .post("/products")
                        .headers(httpHeaders)
                        .content(request.toString());
//        status().isCreated()：驗證 HTTP 狀態碼應為201。讀者可自行探索其他狀態，如 isOk、isNotFound 等。或透過 is 方法直接傳入狀態碼。
//        jsonPath()：獲取指定 JSON 欄位的值。以「$」符號開始，使用「.」符號前往下一層的路徑。
//        hasJsonPath()：驗證某個 JSON 欄位存在。
//        value()：驗證某個 JSON 欄位值為何。
//        header().exists()：驗證回應標頭中的某欄位存在。
//        header().string()：驗證回應標頭中的某欄位值為何。
//        andDO(print())    測試程式的請求與回應詳情印在 Console 視窗。
        mockMvc.perform(requestBuilder)
                .andDo(print())  // System.out::print 不同於此喔請看import
                .andExpect(status().isCreated()) //驗證狀態201
                .andExpect(jsonPath("$.id").hasJsonPath()) //驗證Json是否含有id
                // 如果是 {id:{id_inside:"666"},name:"oni"}這樣 就是要用
                // $.id.id_inside去取得
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                // 通常成功建立資源的話 會在header產生LOCATION 其中包含新增的資源的URI
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE));
                // 預測標頭 MediaType是 JSON  也可以先 Excpet它

    }
    // 下面是第二個寫的測試方法
    @Test// 先不要throws Exception 等等會告知你錯誤
    public void testGetProduct() throws  Exception{
        Product product=createProduct("Economic",450);
        productRepository.insert(product);
        // 上面因為需要創造所以要建立 JSON  這邊單純GET 可以很簡單
        // import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; 要引用這類喔
        mockMvc.perform(
                        get("/products/"+product.getId())
                            .headers(httpHeaders)//這邊需要throws Exception
                )
                //請注意 上方是perform(requestBuilder)再來才是perform(xxx).andExpect
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice())
                );




    }
    // 例如搭配上面的testGetProduct測試使用 為了"方便理解" 雖然根本沒差
    public Product createProduct(String name,Integer price){
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    @Test
    public void testReplaceProduct() throws Exception {
        //每個測試為獨立，所以這邊跟上面是不同的identity
        Product product = createProduct("Economics", 450);
        System.out.println("插入前getId="+product.getId());
        productRepository.insert(product);           //jpa不只改動資料庫 也去動了我的product 讓他值一致 因為object是callbyref 所以可以改動
        System.out.println("插入後getId="+product.getId()); //jpa的好處就是不用手動product=repo.insert();
        JSONObject request=new JSONObject()
                .put("name","Macroeconomics")
                .put("price",550)
                .put("id",product.getId()); // 拿掉會出錯 因為不可以不填入id 在我原始controller寫過

        mockMvc.perform(put("/products/"+product.getId())
                        .headers(httpHeaders)
                        .content(request.toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")));
    }
    //後面表示預期會拋出錯誤 ， 如果沒有寫 那可能會TestFail.
    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception{
        Product product=createProduct("Economics",450);
        productRepository.insert(product);
        mockMvc.perform(delete("/products/"+product.getId())
                                        .headers(httpHeaders))
                .andExpect(status().isNoContent());

        //我我我我我，如果註解我可通過! (沒有@Test(補充內容條件)的狀態下)
        productRepository.findById(product.getId())
                .orElseThrow(RuntimeException::new);

    }

    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception{

        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));
        System.out.println("計算有幾個人在裡面偷玩"+productRepository.count());
//        Arrays.asList(p1, p2, p3, p4, p5).forEach(p-> System.out.println(p));
        mockMvc.perform(get("/products")
                        .headers(httpHeaders)
                        .param("keyword","Manage")
                        .param("orderBy","price")
                        .param("sortRule","asc")
                )
                .andDo(print())
                .andExpect(status().isOk())
                // import static org.hamcrest.Matchers.hasSize;
                // 通常會用靜態來做 比較不會難讀
                .andExpect(jsonPath("$",hasSize(4)))  //用五個會找不到喔 不是沒插入而是因為你只找的到4個含有manage
                .andExpect(jsonPath("$[0].id").value(p2.getId())) //安照規則排序而言就應該如此
                .andExpect(jsonPath("$[1].id").value(p1.getId()))
                .andExpect(jsonPath("$[2].id").value(p4.getId()))
                .andExpect(jsonPath("$[3].id").value(p3.getId()));
        //Body = [{"id":"650d874ef3e06f3a5f0c71d9","name":"Marketing Management","price":200},
        //  {"id":"650d874ef3e06f3a5f0c71d8","name":"Operation Management","price":350},
        //  {"id":"650d874ef3e06f3a5f0c71db","name":"Finance Management","price":400},
        //  {"id":"650d874ef3e06f3a5f0c71da","name":"Human Resource Management","price":420}
    }
    @Test
    public void testSearchProductsSortByPriceAsc_mockSeparate() throws Exception{

        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);
        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));
        System.out.println("計算有幾個人在裡面偷玩"+productRepository.count());
//        Arrays.asList(p1, p2, p3, p4, p5).forEach(p-> System.out.println(p));
        MvcResult result= mockMvc.perform(get("/products")
                                        .headers(httpHeaders)
                                        .param("keyword","Manage")
                                        .param("orderBy","price")
                                        .param("sortRule","asc")
                            ).andReturn();
        MockHttpServletResponse mockResponse=result.getResponse();
        String responseJSONStr=mockResponse.getContentAsString();
        JSONArray productJSONArray=new JSONArray(responseJSONStr);


        List<String> productIds=new ArrayList<>();
        for (int i = 0; i < productJSONArray.length(); i++) {
            JSONObject productJSON = productJSONArray.getJSONObject(i);
            productIds.add(productJSON.getString("id"));
        }


        Assert.assertEquals(4,productIds.size());
        Assert.assertEquals(p2.getId(),productIds.get(0));
        Assert.assertEquals(p1.getId(),productIds.get(1));
        Assert.assertEquals(p4.getId(),productIds.get(2));
        Assert.assertEquals(p3.getId(),productIds.get(3));

        //Assert.assertTrue(productIds.contains(p1.getId()));
        //不想指定排序可以用上面的。

        Assert.assertEquals(HttpStatus.OK.value(),mockResponse.getStatus());
        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                mockResponse.getHeader(HttpHeaders.CONTENT_TYPE)
                );

    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception{
        JSONObject request=new JSONObject()
                .put("name","")
                .put("price",350);

        mockMvc.perform(post("/products")
                        .headers(httpHeaders)
                        .content(request.toString())//#請求內容 body 字串
                )
                .andExpect(status().isBadRequest());

    }
    @Test
    public void get400WhenReplaceProductWithNegativePrice() throws Exception{
        Product product=createProduct("C2C Tutorial",350);
        productRepository.insert(product);

        JSONObject request =new JSONObject()
                .put("id",product.getId())
                .put("name","C2C Tutorial");
//                .put("price",-100)
        //  如果JSON拿掉price  而且Product那邊沒有 @NotNull price
        //  會造成請求被上交 然後status:200，(price:null)




        // 提示: 如果這邊/products/少最後的斜槓 則會404 找不到資源 !
        mockMvc.perform(put("/products/"+product.getId())
                        .headers(httpHeaders)
                        .content(request.toString())
                ).andDo(print())
                .andExpect(status().isBadRequest());

    }



}
