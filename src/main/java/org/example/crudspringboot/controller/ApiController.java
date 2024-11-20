package org.example.crudspringboot.controller;

import org.example.crudspringboot.model.User;
import org.example.crudspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;

    @Autowired
    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String name) {
        List<User> users;
        if (name != null) {
            users = userService.getUsersByName(name);
        } else {
            users = userService.getAllUsers();
        }
        return ResponseEntity.ok().body(users);
    }



}
