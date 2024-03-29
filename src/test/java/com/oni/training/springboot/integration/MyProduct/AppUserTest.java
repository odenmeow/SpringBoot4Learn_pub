package com.oni.training.springboot.integration.MyProduct;


import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.app_user.Role;
import io.jsonwebtoken.Claims;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class AppUserTest extends AppBaseTest {

    private final String URL_USER="/users";


    @Test
    public void testAuthenticate() throws Exception {

        logout();
        AppUserRequest appUserRequest= AppUserRequest.builder()
                .emailAddress("qw28425382694@gmail.com")
                .password(USER_PASSWORD)
                .name("onini666").build();
        AuthResponse authResponse= createUser(appUserRequest);
//        我們要傳入的應該是 AuthRequest 唷
//        System.out.println("注意我"+mapper.writeValueAsString(authResponse));
//        System.out.println("注意我"+mapper.writeValueAsString(appUserRequest));
//        System.out.println("注意我"+appUserRequest.toString());
//        建造AuthRequest作為傳入!

        AuthRequest authRequest= AuthRequest.builder()
                .email(appUserRequest.getEmailAddress())
                .password(appUserRequest.getPassword())
                .build();
        MvcResult body=    mockMvc.perform(
                            MockMvcRequestBuilders.post("/users/authenticate")
                                    .headers(httpHeaders)
//                                    .content(authResponse.toString()))
                                    .content(mapper.writeValueAsString(authRequest)))
                            .andExpect(status().isOk())
                            .andReturn();
        System.out.println(body.getResponse());


    }


    @Test
    public void testCreateUser() throws Exception {
        // 測試 post registry 註冊帳號
        logout();
        AppUserRequest request=
                new AppUserRequest()
                        .builder()
                        .name("OniSan")
                        .emailAddress("qw284211@gmail.com")
                        .role(Arrays.asList(Role.NORMAL))
                        .password("123456")
                        .build();
        MvcResult result=
                mockMvc.perform(
                    MockMvcRequestBuilders.post(URL_USER)
                                .headers(httpHeaders)
                                .content(mapper.writeValueAsString(request))
                        )
                        .andExpect(status().isCreated())
                        .andReturn();
        JSONObject responseBody=
                new JSONObject(result.getResponse().getContentAsString());
        String token=responseBody.getString("token");

        String useEmail=jwtService.extractClaim(token, Claims::getSubject);
        AppUser user=appUserRepository.findByEmailAddress(useEmail)
                            .orElseThrow(RuntimeException::new);
        Assert.assertEquals(request.getEmailAddress(),user.getEmailAddress());
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals(request.getName(),user.getName());
        Assert.assertArrayEquals(request.getRole().toArray(),user.getRole().toArray(new Role[0]));

    }
    @Test
    public void testGetUser() throws Exception{
        AppUserRequest request=
                new AppUserRequest()
                        .builder()
                        .name("OniSan")
                        .emailAddress("qw284211@gmail.com")
                        .role(Arrays.asList(Role.NORMAL))
                        .password("123456")
                        .build();
        AuthResponse user=createUser(request);//創建帳號都是預設NORMAL 無ADMIN
        String email=jwtService.extractUsername(user.getToken());
        AuthRequest authreq=new AuthRequest()
                                    .builder()
                                    .email(request.getEmailAddress())
                                    .password(request.getPassword()).build();
        login(authreq);
        // 由於上面login 接下來的 httpheader都將攜帶token做事
        addRole("ADMIN",authreq.getEmail());
        mockMvc.perform(
            MockMvcRequestBuilders.get("/users/GetUserDetail")
                    .headers(httpHeaders)
                    .param("email",authreq.getEmail())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value(authreq.getEmail()))
//                .andExpect(jsonPath("$.password").value(passwordEncoder.encode(authreq.getPassword())))
//               密碼無法驗證這樣比對 只能透過passwordEncoder.match(raw , passFromRepo) 返回boolean
/**           BCryptEncoder會自己把資料庫的hash的鹽提取出來 然後把match的時候自動添加相同的salt 用以比對。  */


                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.role", IsCollectionWithSize.hasSize(2)))
//                .andExpect(jsonPath("$.role[0]").value(Role.ADMIN.toString()));
//              role[0] [1] 是隨機的進入 所以不能夠上面這樣驗證
                .andExpect(jsonPath("$.role",
                                        Matchers.containsInAnyOrder(
                                                Role.NORMAL.toString(),Role.ADMIN.toString()
                                                )
                                    )
                );





    }
    @Test
    public void test409WhenCreateUserWithExistingEmail() throws Exception {
        AppUserRequest request=new AppUserRequest()
                        .builder()
                        .name("OniSan")
                        .emailAddress("qw284211@gmail.com")
                        .role(Arrays.asList(Role.NORMAL))
                        .password("123456")
                        .build();
        AuthResponse existingUser=createUser(request);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .headers(httpHeaders)
                        .content(mapper.writeValueAsString(request))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict());

    }
    @Test
    public void test404WhenGetNonExistentUser() throws Exception {
        AppUserRequest request=new AppUserRequest()
                .builder()
                .name("OniSan")
                .emailAddress("qw284211@gmail.com")
                .role(Arrays.asList(Role.NORMAL))
                .password("123456")
                .build();
        createUser(request);
        AuthRequest authRequest=AuthRequest.builder()
                                .email(request.getEmailAddress())
                                .password(request.getPassword())
                                .build();

        addRole(Role.ADMIN.toString(), authRequest.getEmail());
        login(authRequest);



        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/GetUserDetail")
                        .headers(httpHeaders)
                        .param("email","qw@gmail")
                ).andExpect(status().isNotFound());

    }



}
