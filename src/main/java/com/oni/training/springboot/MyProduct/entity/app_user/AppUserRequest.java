package com.oni.training.springboot.MyProduct.entity.app_user;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Schema(description = "The email address of the user.", example = "qw28425382694@gmail.com")
    @NotBlank  //非null 非空 非tab  字元不包含前者
    private String emailAddress;

    @Schema(description = "The password of the user", example = "aa1234" )
    @NotBlank
    @Size(min=6,max=20,message = "Should between 6 and 20 characters")
    private String password;

    @NotBlank
    @Schema(description = "The name of the user" , example = "oni")
    private String name;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING) //讓他以字串儲存在資料庫
    @Schema( type = "array",description = "Initially has only USER authority.No use to add this field ")
//    上面沒type會重複生成字段給陣列，下面則是陣列中一個有消息 但還是在陣列內= =
    @ArraySchema(arraySchema = @Schema(type="string",description = "hi,,Initially has only USER authority.No use to add this field "))
    private List<Role> role;

}
