package com.oni.training.springboot.MyProduct.controller;

import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AppUser" ,description = "Authentication and Basic CRUD with User")
public interface AppUserControllerApi {



    @PostMapping("/authenticate")
    @Operation(summary = "Generate a token for you", description = "You will receive a token for the specified email by AuthRequest. We use ResponseEntity<?> , so, swagger not generate the hint format.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Re-gen JWT successfully, I will not find the name for you.",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            """
                                                       {
                                                            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxdzI4NDI1MzgyNjk0QGdtYWlsLmNvbSIsImp0aSI6IjY1ZDM4NDlhMDI2N2RiNzNiMTRiNGY1NiIsImlhdCI6MTcwODQxNzMxOSwiZXhwIjoxNzA4NTAzNzE5fQ.1V_ZOUABlAnm8GZudeU5AZowyt1ekC59TCq2xtw_xew",
                                                                                         "name": null
                                                       }
                                                    """
                                    ))
                    ),  //不寫content會給預設的回傳body 示例
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url, or having the wrong password",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Not found such a user(email) exist" ,
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Against format regulation" ,
                            content = @Content)
//                     content = @Content(schema = @Schema(implementation = AppUserResponse.class)))
//                    根據不同的 狀態回不同的body的話 可以如上描述告知
            }
    )
//    ResponseEntity<AuthResponse> UserLogin(@Valid @RequestBody AuthRequest body, Errors errors);
    ResponseEntity<?> UserLogin(@Valid @RequestBody AuthRequest body, Errors errors);

    @PostMapping
    @Transactional
    @Operation(summary = "Create a User", description = "You will receive a token in the AuthResponse.<br> 麻煩使用自己的emails",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully created"),  //不寫content會給預設的回傳body 示例
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                                description = "Email address is already in use." ,
                            content = @Content)
            }
    )
    ResponseEntity<AuthResponse> createUser(@Valid @RequestBody AppUserRequest request);

    //    @GetMapping("/{id}")
    //    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id){
    //        AppUserResponse user= service.getUserResponseById(id);
    //        return ResponseEntity.ok(user);
    //    }
    @GetMapping("/GetEmails")
    @Operation(summary = "Get emails you have enrolled with an token of ADMIN role", description = "You will receive a List of String(Email)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get Emails",//不寫content會給預設的回傳body 示例
                            // https://stackoverflow.com/questions/67099410/springdoc-openapi-how-to-display-an-array-of-strings-as-a-response
                            // ExampleObject使用範例
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)) ,
                            examples ={
                                    @ExampleObject("[\"qw28425382694@gmail.com\",\"linc4003931@gmail.com\"]"),
                            }

                    )
                    ),

                    @ApiResponse(responseCode = "403",
                            description = "Only user with ADMIN authority can do this ,or Maybe not permit url",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Maybe having no authentication" ,
//                            Possibly no authentication
                            content = @Content)
            }
    )
    ResponseEntity<List<String>> getEmailsByAdminToken();

    @GetMapping("/GetUserDetail")
    @Operation(summary = "Get AppUser by the User's JWTToken and Email, please give me JWT token, just trigger the right-top lock", description = "You will receive the format of AppUser.  ",
//            Fetch the email associated with the User's Token
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get UserDetail"  //不寫content會給預設的回傳body 示例
//                         ,content = @Content  // 這樣才不會什麼東西都沒出現，預設使用responseEntity內的AppUser格式
                            // 由於我有替AppUser 寫 Api @Schema(description...所以直接省略content就對了!
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Maybe having no authentication, or no such email." ,
//                            Possibly no authentication
                            content = @Content)
            }
    )
    ResponseEntity<AppUser> getByEmail(@RequestParam("email") String email);

    @GetMapping(value = "/username")
    @Operation(summary = "Get the username by the given JWTToken, no args, trigger the lock top-right and give me JWT.", description = "You will retrieve the name of the correspond AppUser  ",
//            Fetch the email associated with the User's Token
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get the Username",
                            content = @Content(schema = @Schema(description = "if token is good, returning an email."
                                    ,example = "qw28425382694@gmail.com"))


                    ),  //不寫content會給預設的回傳body 示例
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Maybe having no authentication, or no such token." ,
//                            Possibly no authentication
                            content = @Content)
            }
    )
    String getUsername(Authentication authentication);

    @PutMapping(value = "/test/ByPass/addRole")
    @Operation(summary = "Assign a Role to a specific user (email)", description = "This operation adds a new Role to the AppUser.  ",
//            Fetch the email associated with the User's Token
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully add the role to the AppUser(email) "),  //不寫content會給預設的回傳body 示例
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Maybe having no authentication, or no such an email exists." ,
//                            Possibly no authentication
                            content = @Content)
            }
    )
    ResponseEntity<AppUser> setUserAuthRole(
            @Parameter(name = "email", description = "User email", example = "qw28425382694@gmail.com")
            @RequestParam(value = "email") String email,
            @Parameter(name = "role", description = "give ADMIN or NORMAL", example = "ADMIN")

            @RequestParam(value = "role") String role

    );
}
