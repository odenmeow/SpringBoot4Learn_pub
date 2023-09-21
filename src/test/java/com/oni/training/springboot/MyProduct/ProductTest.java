package com.oni.training.springboot.MyProduct;


import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 因為專案使用Spring框架所以要使用Spring提供的這個
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    // 使用之前請先建立ProductT專用的資料庫跟products喔 避免混亂(雖然重啟都是重設拉哈哈) 練練看
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateProduct() throws Exception{
        //選擇springFramework提供的版本
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
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
                .andExpect(jsonPath("$.id").hasJsonPath()) //驗證
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE));
    }

}
