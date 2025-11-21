package ru.kata.spring.boot_security.demo.configs.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.configs.dao.RoleDao;
import ru.kata.spring.boot_security.demo.configs.dao.UserDao;
import ru.kata.spring.boot_security.demo.configs.model.Role;
import ru.kata.spring.boot_security.demo.configs.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Component
public class DataInitializerRunner implements CommandLineRunner {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerRunner(RoleDao roleDao, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Создаём роли
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");

        // Создаём пользователей
        createUserIfNotExists("admin", "Admin", "admin", "admin", Set.of("ROLE_ADMIN", "ROLE_USER"));
        createUserIfNotExists("user", "User", "user", "user", Set.of("ROLE_USER"));
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleDao.findByName(roleName).isEmpty()) {
            roleDao.save(new Role(roleName));
        }
    }

    private void createUserIfNotExists(String name, String surname, String username, String password, Set<String> roleNames) {
        // Проверяем существование пользователя по имени (name)
        boolean exists = userDao.getAllUsers()
                .stream()
                .anyMatch(u -> u.getName().equals(name));
        if (exists) return;

        // Получаем роли
        Set<Role> roles = roleNames.stream()
                .map(rn -> roleDao.findByName(rn).get())
                .collect(Collectors.toSet());

        // Создаём пользователя
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(username); // можно использовать как логин, если нужно, или просто оставить как name
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(roles);

        // Сохраняем пользователя
        userDao.saveUser(user);
    }
}
