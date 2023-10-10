package com.oni.training.springboot.MyProduct.repository;

import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AppUserRepository extends MongoRepository<AppUser,String> {

    Optional<AppUser> findByEmailAddress(String email);

}
