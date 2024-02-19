package com.oni.training.springboot.MyProduct.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oni.training.springboot.MyProduct.auth.AuthenticationService;
import com.oni.training.springboot.MyProduct.auth.JwtService;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
import com.oni.training.springboot.MyProduct.entity.CustomBadResponse;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUser;
import com.oni.training.springboot.MyProduct.entity.app_user.AppUserRequest;
import com.oni.training.springboot.MyProduct.entity.app_user.Role;
import com.oni.training.springboot.MyProduct.repository.AppUserRepository;
import com.oni.training.springboot.WebExceptions.NotFoundException;
import com.oni.training.springboot.WebExceptions.UnprocessableEntityException;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.callback.PasswordValidationCallback;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController implements AppUserControllerApi {
    //TODO 看能不能添加   1.黑名單功能 2.Token ReLogin  => disable old one.

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService service;
    @Autowired
    private final AppUserRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AppUserController(JwtService jwtService, AuthenticationService service, AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.service = service;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
//     這邊沒有使用 自定義攔截錯誤並回傳的話 就會只出現badRequest 400
//     如果把Error帶入 自己處理並生成的話 會更好，api呼叫者可以知道更詳細的錯誤 !
//    ( 把 @Validate 遇到的 @NonBlank 的消息拋出)
    @Override
    @PostMapping("/authenticate")
    public ResponseEntity<?> UserLogin(@Valid @RequestBody AuthRequest body, Errors errors){
        if(errors.hasErrors()){
            Map<String,String> map=new HashMap<>();
            List<FieldError> fielderrors=errors.getFieldErrors();
            for(var fielderror:fielderrors){
                map.put(fielderror.getField(), fielderror.getDefaultMessage());
            }
            String path=ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();
            CustomBadResponse customBadResponse=new CustomBadResponse(map,path);
//          使用建構式避免Builder，怕預設值沒被用上(畢竟我Builder模式下只有設定timestamp不輸入也有預設)
            return ResponseEntity.badRequest().body(customBadResponse);

        }else {
            System.out.println(repository.findByEmailAddress(body.getEmail()));
            System.out.println("印出從裡面印出的 不見得真有" + body.getEmail());
            AuthResponse response = service.authenticate(body);
            System.out.println(response.getToken());
            return ResponseEntity.ok(response);
        }
    }
    @Override
    @PostMapping
//    @Transactional // runtime不會觸發
    @Transactional(rollbackFor = Exception.class) //故意讓runtime也會被觸發!
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody AppUserRequest request){
        System.out.println("你好");
        AuthResponse user= service.register(request);
        if (true){
            throw new RuntimeException("故意的錯誤");
        }

        String id=jwtService.extractClaim(user.getToken(), Claims::getId);
        URI location= ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(user);
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id){
//        AppUserResponse user= service.getUserResponseById(id);
//        return ResponseEntity.ok(user);
//    }
    @Override
    @GetMapping("/GetEmails")
    public ResponseEntity<List<String>> getEmailsByAdminToken(){
        List<AppUser> users=repository.findAll();
        List<String> emails=users.stream().map(AppUser::getEmailAddress).collect(Collectors.toList());
        return ResponseEntity.ok(emails);
    }
    @Override
    @GetMapping("/GetUserDetail")
    public ResponseEntity<AppUser> getByEmail(@RequestParam("email") String email){
        Optional<AppUser> optionalAppUserUser=repository.findByEmailAddress(email);
        AppUser appUser= optionalAppUserUser.orElseThrow(()->new NotFoundException("找不到該用戶"));
        return ResponseEntity.ok(appUser);
    }
    @Override
    @GetMapping(value = "/username")
    public String getUsername(Authentication authentication) {
        String username = authentication.getName();
        System.out.println(username); // user
        return username;
    }
    @Override
    @GetMapping(value = "/test/ByPass/addRole")
    public ResponseEntity<AppUser> setUserAuthRole(@RequestParam(value = "email") String email,
                                                   @RequestParam(value = "role") String role

    ){
       Optional <AppUser> opuser=repository.findByEmailAddress(email);
//       AppUser user= opuser.orElseThrow();    這樣才會console 顯示錯誤 (如果 /error沒有 permitall )

       AppUser user= opuser.orElseThrow(()->new NotFoundException("no such an email."));

       List<Role> userRole= user.getRole();
       Set<Role> RoleSet=userRole.stream().collect(Collectors.toSet());

       switch (role.toUpperCase()){
           case "ADMIN":
               RoleSet.add(Role.ADMIN);
               break;
           case "NORMAL":
               RoleSet.add(Role.NORMAL);
               break;
           default:
//               throw new IllegalArgumentException("no such a Role");  這種的會拋出原生錯誤 (非web 也就是InternalError)
               throw new UnprocessableEntityException("無法添加，不存在Role: "+role);
       }

       user.setRole(RoleSet.stream().toList()); // 因為沒有要改動了所以沒差 丟進去就是字而已對吧
       repository.save(user);
       return ResponseEntity.ok(user);
    }

}
