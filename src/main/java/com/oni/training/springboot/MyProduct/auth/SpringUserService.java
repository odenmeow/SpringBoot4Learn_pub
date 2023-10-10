package com.oni.training.springboot.MyProduct.auth;

import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserService;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SpringUserService implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

    @Override  // 通常 springService 安全使用的username就是指 email !

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser appUser=appUserService.getUserByEmail(username);
            return new SpringUser(appUser);
        }catch (NotFoundException e){
            throw new UsernameNotFoundException("Username is wrong.");
        }

    }
}
