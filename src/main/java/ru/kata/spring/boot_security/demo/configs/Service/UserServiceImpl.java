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
import ru.kata.spring.boot_security.demo.configs.util.UserNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        users.forEach(u -> u.getRole().size());
        return users;
    }

    @Override
    @Transactional
    public User saveOrUpdateUser(User user, List<Long> roleIds) {
        Set<Role> roles = roleIds.stream().map(roleDao::getRoleById).collect(Collectors.toSet());
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(roles);
            userDao.saveUser(user);
            return user;
        } else {
            User existingUser = userDao.getUser(user.getId());
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(roles);
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userDao.saveUser(existingUser);
            return existingUser;
        }
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        User user = userDao.getUser(id);
        if (user == null) throw new UserNotFoundException();
        user.getRole().size();
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.findByName(name)
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
