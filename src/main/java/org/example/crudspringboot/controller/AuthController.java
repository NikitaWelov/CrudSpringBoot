package org.example.crudspringboot.controller;

import jakarta.transaction.Transactional;
import org.example.crudspringboot.config.security.UserValidator;
import org.example.crudspringboot.model.User;
import org.example.crudspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    @Transactional
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.registerUser(user);
        } catch (IllegalStateException e) {
            return "redirect:/registration?error=exists";
        }
        return "redirect:/login";
    }
}