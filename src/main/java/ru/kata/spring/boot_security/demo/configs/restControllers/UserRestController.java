package ru.kata.spring.boot_security.demo.configs.restControllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.configs.Service.UserService;
import ru.kata.spring.boot_security.demo.configs.dao.RoleDao;
import ru.kata.spring.boot_security.demo.configs.dto.UserDTO;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;
import ru.kata.spring.boot_security.demo.configs.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.configs.util.UserNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final RoleDao roleDao;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRestController(UserService userService, RoleDao roleDao, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleDao = roleDao;
        this.modelMapper = modelMapper;
    }

    // Получить всех пользователей
    @GetMapping
    public List<UserDTO> showAllUsers() {
        return userService.getAllUsers().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    // Получить одного пользователя
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable long id) {
        return convertToUserDTO(userService.getUser(id));
    }

    // Создать пользователя
    @PostMapping
    public UserDTO saveUser(@RequestBody @Valid UserDTO userDTO) {
        User user = convertToUser(userDTO);
        User savedUser = userService.saveOrUpdateUser(user, userDTO.getRoleIds());
        return convertToUserDTO(savedUser);
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable long id, @RequestBody @Valid UserDTO userDTO) {
        userDTO.setId(id);
        User user = convertToUser(userDTO);
        User updatedUser = userService.saveOrUpdateUser(user, userDTO.getRoleIds());
        return convertToUserDTO(updatedUser);
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    // Конвертеры
    private User convertToUser(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        return user;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);

        if (user.getRole() != null) {
            dto.setRoles(user.getRole().stream().map(Role::getRole).collect(Collectors.toList()));
            dto.setRoleIds(user.getRole().stream().map(Role::getId).collect(Collectors.toList()));
        }

        return dto;
    }

    // Ошибка
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("Пользователь не найден", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
