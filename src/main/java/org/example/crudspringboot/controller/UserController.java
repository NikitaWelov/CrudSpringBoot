package org.example.crudspringboot.controller;

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

    /**
     * Возвращает HTML-страницу пользователей
     */
    @GetMapping
    public String getUsersHTML() {
        return "users";
    }

    /**
     * Возвращает список всех пользователей
     */
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Обновление информации о пользователе
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление ролей пользователя
     */
    @PutMapping("/{id}/roles")
    @ResponseBody
    public ResponseEntity<Void> updateUserRoles(
            @PathVariable Long id,
            @RequestBody Set<String> roles) {
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
