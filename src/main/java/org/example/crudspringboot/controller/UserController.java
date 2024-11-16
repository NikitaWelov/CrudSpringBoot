package org.example.crudspringboot.controller;

import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.RoleType;
import org.example.crudspringboot.model.User;
import org.example.crudspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String getUsersHTML() {
        return "users";
    }

    //TODO: добавить метод для принятия @PutMapping("/{id}/roles") updateUserRoles(@PathVariable Long id, @RequestBody Set<RoleType> roles)

    @PutMapping("/{id}/roles")
    public ResponseEntity<String> updateUserRoles(@PathVariable Long id, @RequestBody Set<Role> roles) {
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(user);
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User user1 = userService.getUserById(id);
        if (user1 != null) {
            userService.updateUser(user);
        }
        return ResponseEntity.ok().body(user);
    }

}
