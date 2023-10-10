package com.oni.training.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oni.training.springboot.MyProduct.auth.AuthenticationService;
import com.oni.training.springboot.MyProduct.auth.JwtService;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.app_user.Role;
import com.oni.training.springboot.MyProduct.entity.product.Product;
import com.oni.training.springboot.MyProduct.entity.product.ProductRequest;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;


@RunWith(SpringRunner.class)
//@RunWith(SpringRunner.class)：用于在Spring应用程序中运行测试。它允许测试类与Spring框架集成，并使用Spring的功能，如依赖注入和事务管理。
@SpringBootTest  //因為這個所以會啟用 @SpringBootApplication 這對像 啟動整個項目
@AutoConfigureMockMvc
public class AppBaseTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    protected AppUserRepository appUserRepository;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected HttpHeaders httpHeaders;
    protected final ObjectMapper mapper=new ObjectMapper();



    @Before
    public void init(){
        httpHeaders=new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
    @After
    @Before
    public void clearDB(){
        productRepository.deleteAll();
        appUserRepository.deleteAll();
        System.out.println("清空 TEST 資料庫");
    }
    protected AuthResponse createUser(AppUserRequest appUserRequest) throws Exception {
        MvcResult result=mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(appUserRequest)))
                        .andReturn();
        String responseJsonStr=result.getResponse().getContentAsString();
//        JSONObject jsonObject=new JSONObject(responseJsonStr);
//        jsonObject.getString("toekn");
//                authenticationService.register(appUserRequest)
        return mapper.readValue(responseJsonStr,AuthResponse.class);
    }

    protected void login(AuthRequest authRequest) throws Exception {
        MvcResult result=
                mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users/authenticate")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(authRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONObject parseResult=
                new JSONObject(result.getResponse().getContentAsString());

        String token=parseResult.getString("token");
        httpHeaders.add(HttpHeaders.AUTHORIZATION,"Bearer "+token);
    }
    protected void logout(){
        httpHeaders.remove(HttpHeaders.AUTHORIZATION);
    }

    protected void addRole(String role,String useremail) throws Exception {
        MvcResult result=
                mockMvc.perform(
                        MockMvcRequestBuilders
                        .get("/users/test/ByPass/addRole")
                        .headers(httpHeaders)
                        .param("email",useremail)
                        .param("role",role)
                )
                        .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk()) //200 ok
                .andReturn();

        JSONObject parseResult=
                new JSONObject(result.getResponse().getContentAsString());

        String prettyJson=parseResult.toString();
        System.out.println("成功授權: "+prettyJson);
    }

}
