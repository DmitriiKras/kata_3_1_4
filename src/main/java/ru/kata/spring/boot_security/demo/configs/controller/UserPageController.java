package ru.kata.spring.boot_security.demo.configs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserPageController {

    @GetMapping
    public String userPage() {
        return "user"; // просто загружает HTML
    }
}