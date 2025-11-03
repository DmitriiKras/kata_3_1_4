package ru.kata.spring.boot_security.demo.configs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.configs.Service.UserService;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        model.addAttribute("allRoles", userService.getAllRoles());

        User admin = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", admin);
        return "admin";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value="roleIds", required=false) List<Long> roleIds) {
        userService.saveOrUpdateUser(user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model, Principal principal) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        model.addAttribute("allRoles", userService.getAllRoles());

        // Текущий пользователь (для шапки и для вкладки User)
        User currentUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", currentUser);

        return "admin";
    }
}
