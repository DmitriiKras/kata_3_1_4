package ru.kata.spring.boot_security.demo.configs.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.configs.Service.UserService;
import ru.kata.spring.boot_security.demo.configs.dto.UserDTO;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CurrentUserRestController {

    private final UserService userService;

    @Autowired
    public CurrentUserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public UserDTO getCurrentUser(Principal principal) {

        User user = (User) userService.loadUserByUsername(principal.getName());

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());

        dto.setRoles(
                user.getRole().stream()
                        .map(Role::getRole)
                        .collect(Collectors.toList())
        );

        dto.setRoleIds(
                user.getRole().stream()
                        .map(Role::getId)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
