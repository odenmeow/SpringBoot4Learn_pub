package com.oni.training.springboot.Ch24.controller;

import com.oni.training.springboot.Ch24.exception.OperateAbsentItemsException;
import com.oni.training.springboot.Ch24.model.BatchDeleteRequest;
import com.oni.training.springboot.Ch24.model.UserTF;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/ch24/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserControllerCH24 {
    private final Map<String, UserTF> userDB = new LinkedHashMap<>();
    @PostConstruct
    private void initData(){
        var users= List.of(
                UserTF.of("U1","Oni","qw28425382694@gmail.com"),
                UserTF.of("U2","Umi","qw28425382@gmail.com"),
                UserTF.of("U1","Oni","linc4003931@gmail.com")
        );
        users.forEach(u->userDB.put(u.getId(),u));
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@RequestBody BatchDeleteRequest request){
        var itemIds=request.getIds();
        var absentIds=itemIds.stream()
                .filter(Predicate.not(userDB::containsKey))
                .collect(Collectors.toList());
        if(!absentIds.isEmpty()){
            // 有缺席就丟錯誤
            throw new OperateAbsentItemsException(absentIds);
        }
        itemIds.forEach(userDB::remove);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<UserTF>> getUsers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String email){

        var stream=userDB.values().stream();
        if(name!=null)
            stream=stream.filter(u->u.getName().toLowerCase().contains(name));
        if(email!=null)
            stream=stream.filter(u->u.getEmail().toLowerCase().equalsIgnoreCase(email));
        var users = stream.collect(Collectors.toList());
        return ResponseEntity.ok(users);

    }
}
