package com.oni.training.springboot.MyProduct.entity.app_user;

import com.oni.training.springboot.MyProduct.converter.AppUserConverter;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserResponse;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }
    public AppUserResponse createUser(AppUserRequest request){

        Optional<AppUser> existingUser= repository.findByEmailAddress(request.getEmailAddress());

        existingUser.orElseThrow(()-> new UnprocessableEntityException("該Email已經被使用"));
        // 如果可以沒引發異常那就代表email通過了，可以繼續創立帳號。
        AppUser appUser=AppUserConverter.toAppUser(request);
        appUser=repository.insert(appUser);
        // 不特意賦值也ok ，jpa 創建後會自動同步更新。
        return AppUserConverter.toAppUserResponse(appUser);
    }
    public AppUserResponse getUserResponseById(String id){
        AppUser user=repository.findById(id)
                .orElseThrow(()->new NotFoundException("no such user."));
        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUser getUserByEmail(String email){
        return repository.findByEmailAddress(email)
                .orElseThrow(()->new NotFoundException("no matched user."));

    }
    public List<AppUserResponse> getUserResponses(){
        List<AppUser> users=repository.findAll();
        return AppUserConverter.toAppUserResponses(users);
    }
}
