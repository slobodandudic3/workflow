package com.slober.workflow.controller;


import com.slober.workflow.model.User;
import com.slober.workflow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
