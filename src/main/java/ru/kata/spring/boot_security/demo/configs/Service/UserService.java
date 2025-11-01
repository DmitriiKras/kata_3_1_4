package ru.kata.spring.boot_security.demo.configs.Service;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.util.List;

public interface UserService {

    public List<User> getAllUsers();
    public void saveOrUpdateUser(User user, List<Long>  roleIds);
    public User getUser(long id);
    public void deleteUser(long id);
    public UserDetails loadUserByUsername(String username);
    public List<Role> getAllRoles();
    Role getRoleById(Long id);
}
