package com.oni.training.springboot.MyProduct.converter;

import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserConverter {

    private AppUserConverter(){

    }
    public static AppUser toAppUser(AppUserRequest request){
        AppUser user=new AppUser();

        user.setEmailAddress(request.getEmailAddress());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }
    public static AppUserResponse toAppUserResponse(AppUser user){
        AppUserResponse response=new AppUserResponse();
        response.setRole(user.getRole());
        response.setId(user.getId());
        response.setEmailAddress(user.getEmailAddress());
        response.setName(user.getName());
        return response;
    }

    public static List<AppUserResponse> toAppUserResponses(List<AppUser> users){
        return users.stream()
                .map(AppUserConverter::toAppUserResponse)
                .collect(Collectors.toList());
    }

}
