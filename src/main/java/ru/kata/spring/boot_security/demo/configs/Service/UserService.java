package ru.kata.spring.boot_security.demo.configs.Service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User saveOrUpdateUser(User user, List<Long> roleIds);
    User getUser(Long id);
    void deleteUser(Long id);
    UserDetails loadUserByUsername(String name); // теперь по name
    List<Role> getAllRoles();
    Role getRoleById(Long id);
}
