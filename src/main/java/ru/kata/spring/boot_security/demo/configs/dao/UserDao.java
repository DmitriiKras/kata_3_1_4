package ru.kata.spring.boot_security.demo.configs.dao;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAllUsers();

    public void saveUser(User user);

    public User getUser(long id);

    public void deleteUser(long id);

    Optional<User> findByName(String name);
}
