package ru.kata.spring.boot_security.demo.configs.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.configs.dao.RoleDao;
import ru.kata.spring.boot_security.demo.configs.dao.UserDao;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao1, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao1;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user, List<Long> roleIds) {
        User existingUser = null;

        if (user.getId() != null) {
            existingUser = userDao.getUser(user.getId());
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            } else {
                throw new IllegalArgumentException("Password cannot be empty for new user");
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (roleIds == null || roleIds.isEmpty()) {
            if (existingUser != null) {
                user.setRole(existingUser.getRole());
            }
        } else {
            Set<Role> roles = roleIds.stream()
                    .map(roleDao::getRoleById)
                    .filter(r -> r != null)
                    .collect(Collectors.toSet());
            user.setRole(roles);
        }
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public User getUser(long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getRole().size();
        return user;
    }
    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.getRoleById(id);
    }
}
