package ru.kata.spring.boot_security.demo.configs.dto;
import ru.kata.spring.boot_security.demo.configs.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<String> roles;
    private List<Long> roleIds;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRole().stream()
                .map(r -> r.getRole())
                .collect(Collectors.toList());
        this.roleIds = user.getRole().stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());
    }
    public UserDTO() {

    }


    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Long> getRoleIds() {
            return roleIds;
        }
    public void setRoleIds(List<Long> roleIds) {
            this.roleIds = roleIds;
    }
}
