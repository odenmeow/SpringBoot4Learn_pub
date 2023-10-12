package com.oni.training.springboot.MyProduct.auth.auth_user;


import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// 透過上下文取得對象 專門辨別使用者 的class
@Component
public class UserIdentity {

    private final SpringUser EMPTY_USER=new SpringUser(new AppUser());

    private SpringUser getSpringUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Object principal=authentication.getPrincipal();
        return "anonymousUser".equals(principal)
                        ?EMPTY_USER
                        :(SpringUser)principal;
    }
    public boolean isAnonymous(){
        return EMPTY_USER.equals(getSpringUser());
    }
    public String getId(){
        return  getSpringUser().getId();
    }
    public String getName(){
        return getSpringUser().getName();
    }
    public String getEmail() {
        return getSpringUser().getUsername();
    }

}

