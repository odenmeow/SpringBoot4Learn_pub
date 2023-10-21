package com.oni.training.springboot.MyProduct.controller;


import com.oni.training.springboot.MyProduct.auth.AuthenticationService;
import com.oni.training.springboot.MyProduct.auth.JwtService;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthRequest;
import com.oni.training.springboot.MyProduct.auth.auth_user.AuthResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {
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
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> UserLogin(@Valid @RequestBody AuthRequest body){
        System.out.println(repository.findByEmailAddress(body.getEmail()));
        System.out.println("印出從裡面印出的 不見得真有"+body.getEmail());
        AuthResponse response=service.authenticate(body);
        System.out.println(response.getToken());
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody AppUserRequest request){

        AuthResponse user= service.register(request);
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
    @GetMapping("/GetEmails")
    public ResponseEntity<List<String>> getEmailsByAdminToken(){
        List<AppUser> users=repository.findAll();
        List<String> emails=users.stream().map(AppUser::getEmailAddress).collect(Collectors.toList());
        return ResponseEntity.ok(emails);
    }
    @GetMapping("/GetUserDetail")
    public ResponseEntity<AppUser> getByEmail(@RequestParam("email") String email){
        Optional<AppUser> optionalAppUserUser=repository.findByEmailAddress(email);
        AppUser appUser= optionalAppUserUser.orElseThrow(()->new NotFoundException("找不到該用戶"));
        return ResponseEntity.ok(appUser);
    }
    @GetMapping(value = "/username")
    public String getUsername(Authentication authentication) {
        String username = authentication.getName();
        System.out.println(username); // user
        return username;
    }
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
