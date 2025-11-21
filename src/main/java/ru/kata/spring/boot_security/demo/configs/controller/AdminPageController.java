package ru.kata.spring.boot_security.demo.configs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.configs.Service.UserService;
import ru.kata.spring.boot_security.demo.configs.dto.UserDTO;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final UserService userService;

    public AdminPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminPage(Model model, Principal principal) {
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        List<Role> allRoles = userService.getAllRoles();
        List<UserDTO> allUsers = userService.getAllUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        model.addAttribute("user", currentUser);
        model.addAttribute("users", allUsers);
        model.addAttribute("allRoles", allRoles);
        return "admin";
    }
}
