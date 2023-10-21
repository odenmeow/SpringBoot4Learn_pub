package com.oni.training.springboot.unit;


import com.oni.training.springboot.MyProduct.auth.ApplicationConfig;
import com.oni.training.springboot.MyProduct.auth.SpringUserService;
import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserService;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;


// @RunWith(SpringRunner.class)  使用這個沒有用 因為這邊玩的是mock
// 不想要自己配置 想使用springboot 使用的@Autowired UserdetailsService的話

// 下面兩個都會依賴外部 ， 一個是依賴部分 一個是完全載入
// @ContextConfiguration(classes = ApplicationConfig.class) // <先提到，帶過就好> 不使用也能pass
// @SpringBootTest 使用這個會啟動整個專案(使用TEST 的 properties 供測試)

@RunWith(MockitoJUnitRunner.class)   //總之發現只需要這個就可以模擬測試Spring Bean了
public class UserServiceTest {

//    @Mock的成員變數會被注入mock物件，也就是假的物件。
//    @InjectMocks標記的成員變數會被注入被標註@Mock的mock物件。
//    適合 @component @service @configuration 之類
//    works for direct member but not include transitive dependencies.
//    @InjectMock 會真的去找對像來注入 但是跟上述說的一樣 只會引入第一層相依，更多就無效。
    @Mock
    private AppUserRepository repository; // 被塞進去applicationConfig <以模擬之姿>
    @InjectMocks

    private ApplicationConfig applicationConfig; // 依賴的依賴不會被處理 這一點要小心喔

    /*
            使用 @Autowired  然後拿除@InjectMocks改用 @ContextConfiguration +@Before => @PosrConstruct
            確實會注入 applicationConfig不至於null
            但是 mock的repository 就無效了(不會注入到InjectMocks) ，這會導致後續的失靈
            也就是 後面使用 loadUserbyUserName的方法的時候 原本會使用repository.findByEmailAddress....(這個repo原本是注入的)
            會被替換 但是沒有用InjectMock 就不會被替換進去 而是使用原始天然的
            加上 沒啟用SpringBootTest 沒跟資料庫連線 所以也不可能找到AppUser對象 直接壞掉
     * */


    @Mock
    private UserDetailsService userDetailsService;
    @Before
    public void setUp(){
        userDetailsService=applicationConfig.userDetailsService();
    }
    @Test
    public void testLoadSpringUser(){

        String email="Oni@gmail.com";
        AppUser appUser=new AppUser();
        appUser.setId("123");
        appUser.setEmailAddress(email);
        appUser.setName("OniSan");
        Mockito.when(repository.findByEmailAddress(email))
                .thenReturn(Optional.of(appUser));
        SpringUser springUser=(SpringUser) userDetailsService.loadUserByUsername(email);

        Assert.assertEquals(appUser.getId(),springUser.getId());
        Assert.assertEquals(appUser.getName(),springUser.getName());
        Assert.assertEquals(appUser.getEmailAddress(),springUser.getUsername());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadSpringUserButNotFound(){
//        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())) 不是你啊 是下面!!!!
        Mockito.when(repository.findByEmailAddress(Mockito.anyString()))
                .thenThrow(new NotFoundException("找不到"));  //為什麼不管丟什麼錯誤都會被變成UsernameNotFoundException
                                                                            //現在丟NoSuchElementException 會出錯了!!
        userDetailsService.loadUserByUsername("oni@gmail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAuthoritiesAsNull(){
        AppUser user = Mockito.mock(AppUser.class);
        Mockito.doThrow(new IllegalArgumentException())
                .when(user).setRole(Mockito.isNull());
        user.setRole(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAuthoritiesAsEmpty(){
        AppUser user= Mockito.mock(AppUser.class);
        Mockito.doThrow(new IllegalArgumentException())
                .when(user).setRole(Collections.emptyList());
        user.setRole(new ArrayList<>());
    }



}
