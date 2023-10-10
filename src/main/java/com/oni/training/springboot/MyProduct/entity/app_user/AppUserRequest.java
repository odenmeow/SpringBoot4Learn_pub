package com.oni.training.springboot.MyProduct.entity.app_user;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRequest {

//user1.setUsername(""); // 不能通过 @NotBlank 验证
//user2.setUsername("   "); // 不能通过 @NotBlank 验证
//user3.setUsername("AB\tCD"); // 通常不能通过 @NotBlank 验证，因为包含制表符
//user4.setUsername("AB\nCD"); // 通常不能通过 @NotBlank 验证，因为包含换行符
    @NotBlank  //非null 非空 非tab  字元不包含前者
    private String emailAddress;
    @NotBlank
    private String password;
    @NotBlank
    private String name;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING) //讓他以字串儲存在資料庫
    private List<Role> role;

}
