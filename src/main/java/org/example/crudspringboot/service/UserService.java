package org.example.crudspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.example.crudspringboot.dao.RoleDao;
import org.example.crudspringboot.dao.UserDao;
import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users...");
        List<User> users = userDao.findAll();
        log.info("Found {} users", users.size());
        return users;
    }

    public void updateUser(Long id, User updatedUser) {
        log.info("Updating user with ID: {}", id);
        User user = userDao.findById(id);

        if (user == null) {
            log.error("User with ID: {} not found", id);
            throw new RuntimeException("User not found");
        }

        log.info("Updating user details: {}", updatedUser);
        user.setUsername(updatedUser.getUsername());
        user.setLastname(updatedUser.getLastname());
        user.setEmail(updatedUser.getEmail());
        userDao.save(user);
        log.info("User with ID: {} successfully updated", id);
    }

    public void updateUserRoles(Long id, Set<String> roleNames) {
        log.info("Updating roles for user with ID: {}", id);
        User user = userDao.findById(id);

        if (user == null) {
            log.error("User with ID: {} not found", id);
            throw new RuntimeException("User not found");
        }

        log.info("Roles to be updated: {}", roleNames);
        Set<Role> roles = roleNames.stream()
                .map(roleName -> {
                    Role role = roleDao.findByName(roleName);
                    if (role == null) {
                        log.error("Role not found: {}", roleName);
                        throw new RuntimeException("Role not found: " + roleName);
                    }
                    log.info("Found role: {}", roleName);
                    return role;
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userDao.save(user);
        log.info("Roles for user with ID: {} successfully updated", id);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (userDao.findById(id) == null) {
            log.error("User with ID: {} not found", id);
            throw new RuntimeException("User not found");
        }
        userDao.deleteById(id);
        log.info("User with ID: {} successfully deleted", id);
    }

    // ???????????
    public void saveUser(User user) {
        log.info("Creating user: {}", user);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
          Role defaultRole = roleDao.findByName("USER");
          if (defaultRole != null) {
              user.setRoles(new HashSet<>(Collections.singletonList(defaultRole)));
          } else {
              log.warn("Default role 'USER' not found. User created without roles.");
          }
        }
        userDao.save(user);
        log.info("User with ID: {} successfully created", user.getId());
    }

    public void createAdmin(String username, String password) {
        log.info("Creating admin user: {}", username);
        Role adminRole = roleDao.findByName("ADMIN");
        if (adminRole == null) {
            log.error("Admin role 'ADMIN' not found. User created without roles.");
            throw new RuntimeException("Admin role 'ADMIN' not found");
        }

        User adminUser = new User();
        adminUser.setUsername(username);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        userDao.save(adminUser);
        log.info("User with ID: {} successfully created", adminUser.getId());
    }

    public User getByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
