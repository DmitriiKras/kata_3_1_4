package ru.kata.spring.boot_security.demo.configs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.configs.Service.UserService;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.security.Principal;

@Controller
public class UserPageController {
    private final UserService userService;

    @Autowired
    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        String username = principal.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "user";
    }
}
