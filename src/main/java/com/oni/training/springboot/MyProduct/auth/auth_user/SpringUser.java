package com.oni.training.springboot.MyProduct.auth.auth_user;

import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringUser implements UserDetails {

    // @NonNull private lombok 才幫做建構式 或
    // private final    也可以
    @NonNull
    private AppUser appUser;



    public String getId(){
        return appUser.getId();
    }
    public String getName(){
        return appUser.getName();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 把之前角色List<UserAuthority> 傳入 打包 一個角色作用一個新的new SGA(name)
//      // 然後回傳這個的Collection 我們是toList() List是種collection~
        return appUser.getRole().stream()
                .map(auth->new SimpleGrantedAuthority(auth.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getEmailAddress(); //注意springUsername視為email!
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
