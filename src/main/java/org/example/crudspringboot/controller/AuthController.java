package org.example.crudspringboot.controller;

import jakarta.transaction.Transactional;
import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.RoleType;
import org.example.crudspringboot.model.User;
import org.example.crudspringboot.service.RoleService;
import org.example.crudspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    @Transactional
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if (userService.getByUsername(username) != null) {
            return "redirect:/registration?error=exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // Назначаем роль USER по умолчанию
        Role userRole = roleService.getDefaultRole();
        user.setRoles(Collections.singleton(userRole));

        // Сохраняем пользователя
        userService.saveUser(user);

        return "redirect:/login";
    }
}