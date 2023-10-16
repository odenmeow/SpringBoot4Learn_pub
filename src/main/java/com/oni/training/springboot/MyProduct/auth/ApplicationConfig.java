package com.oni.training.springboot.MyProduct.auth;

import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Autowired
    private final AppUserRepository repository;


    // 下面有個夥伴 SpringUserService (基本一模一樣但是下面這位有在authenticationProvider() 中被調用，前者則無人調用)
    @Bean
    public UserDetailsService userDetailsService(){


        return username ->{
            AppUser appUser;
            try {
                 appUser = repository
                        .findByEmailAddress(username) //get()會拋出NoSuchAElement
                          .orElseThrow(() -> new NotFoundException("User not found."));

                return new SpringUser(appUser);
            }catch (NotFoundException e){
                throw  new UsernameNotFoundException("Username is wrong.");
            }

        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(@Autowired AuthenticationConfiguration config)
        throws  Exception{            //上方為singleton但不啟用代理 @proxyBeanMode=false
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
