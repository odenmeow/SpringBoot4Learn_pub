package com.oni.training.springboot.MyProduct.entity.app_user;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserResponse {
    private String id;
    private String emailAddress;
    private String name;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING) //讓他以字串儲存在資料庫
    private List<Role> role;

}
