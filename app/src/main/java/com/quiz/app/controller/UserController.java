package com.quiz.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.app.model.User;
import com.quiz.app.service.UserService;

@RestController
public class UserController {
    
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.registerUser(user);
    }
    @GetMapping("/user")
    public List<User> getUser() {
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.getAllUser();
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }
}
