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
    public void run(String... args) throws Exception {
        if (roleDao.findByName("ROLE_ADMIN").isEmpty()) {
            roleDao.save(new Role("ROLE_ADMIN"));
        }
        if (roleDao.findByName("ROLE_USER").isEmpty()) {
            roleDao.save(new Role("ROLE_USER"));
        }

        if (userDao.findByUsername("admin").isEmpty()) {
            Role adminRole = roleDao.findByName("ROLE_ADMIN").get();
            Role userRole = roleDao.findByName("ROLE_USER").get();

            User admin = new User();
            admin.setName("admin");
            admin.setSurname("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Set.of(adminRole, userRole));
            userDao.saveUser(admin);
        }

        if (userDao.findByUsername("user").isEmpty()) {
            Role userRole = roleDao.findByName("ROLE_USER").get();

            User user = new User();
            user.setName("user");
            user.setSurname("User");
            user.setEmail("user@mail.com");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRole(Set.of(userRole));
            userDao.saveUser(user);
        }
    }
}