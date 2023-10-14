package com.oni.training.springboot.integration.AOP_CGLIB_JDK;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CGLIB_JDK_TEST {

    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;

    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    @Before
    public void init(){
        httpHeaders=new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void testAOP() throws Exception {


        RequestBuilder
                requestBuilder= MockMvcRequestBuilders.get("/AOP/jdk");
        for (int i=0;i<2500;i++){mockMvc.perform(requestBuilder);}
            requestBuilder= MockMvcRequestBuilders.get("/AOP/cGlib");
        for (int i=0;i<2500;i++){mockMvc.perform(requestBuilder); }



        requestBuilder= MockMvcRequestBuilders.get("/AOP/avg");
        mockMvc.perform(requestBuilder);
        System.out.println("\n前面為1v1 ");


//                .andDo(MockMvcResultHandlers.print());
    }

//    分開測試 因為JVM 可能自動優化導致 所花時間不太精準
//    分開測試 得出來的確實 介面更多的要花更多調度+生成時間!
    @Test
    public void testAOP_500() throws Exception {


        RequestBuilder
            requestBuilder= MockMvcRequestBuilders.get("/AOP/500/jdk");
        for (int i=0;i<2500;i++){mockMvc.perform(requestBuilder);}
        requestBuilder= MockMvcRequestBuilders.get("/AOP/500/cGlib");
        for (int i=0;i<2500;i++){mockMvc.perform(requestBuilder); }

        System.out.println("\n後面為 500v500");
        requestBuilder= MockMvcRequestBuilders.get("/AOP/500/avg");
        mockMvc.perform(requestBuilder);


//                .andDo(MockMvcResultHandlers.print());
    }






}
