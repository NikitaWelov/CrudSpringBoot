package org.example.crudspringboot.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.crudspringboot.dao.RoleDao;
import org.example.crudspringboot.dao.UserDao;
import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.RoleType;
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

    @Transactional
    public List<User> getAllUsers() {
        log.info("Fetching all users...");
        List<User> users = userDao.findAll();
        log.info("Found {} users", users.size());
        return users;
    }

    @Transactional
    public void updateUser(Long id, User updatedUser) {
        log.info("Updating user with ID: {}", id);
        User user = userDao.findById(id);

        if (user == null) {
            log.error("User with ID: {} not found", id);
            throw new RuntimeException("User not found");
        }
        log.info("Updating user details: {}", updatedUser);
        if (updatedUser.getUsername() != null)
            user.setUsername(updatedUser.getUsername());
        if (updatedUser.getLastname() != null)
            user.setLastname(updatedUser.getLastname());
        if (updatedUser.getEmail() != null)
            user.setEmail(updatedUser.getEmail());
        userDao.save(user);
        log.info("User with ID: {} successfully updated", id);
    }

    @Transactional
    public void updateUserRoles(Long id, Set<String> roleNames) {
        log.info("Updating roles for user with ID: {}", id);
        User user = userDao.findById(id);

        if (user == null) {
            log.error("User with ID: {} not found", id);
            throw new RuntimeException("User not found");
        }
        log.info("Updating user details");


        log.info("Roles to be updated: {}", roleNames);
        Set<Role> roles = roleNames.stream()
                .map(roleName -> {
                    Role role = roleDao.findByName(RoleType.valueOf(roleName));
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



    @Transactional
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
    @Transactional
    public void saveUser(User user) {
        log.info("Creating user: {}", user);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
          Role defaultRole = roleDao.findByName(RoleType.USER);
          if (defaultRole != null) {
              user.setRoles(new HashSet<>(Collections.singletonList(defaultRole)));
          } else {
              log.warn("Default role 'USER' not found. User created without roles.");
          }
        }
        userDao.save(user);
        log.info("User with ID: {} successfully created", user.getId());
    }

    @Transactional
    public void createAdmin(String username, String password) {
        log.info("Creating admin user: {}", username);
        Role adminRole = roleDao.findByName(RoleType.USER);
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

//    @PostConstruct
//    public void init() {
//        if (userDao.findByUsername("admin") == null) {
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword("admin");
//
//            Role adminRole = roleDao.findByName(RoleType.ADMIN);
//            if (adminRole == null) {
//                adminRole = new Role();
//                adminRole.setName(RoleType.ADMIN);
//                roleDao.save(adminRole);
//            }
//            admin.setRoles(Set.of(adminRole));
//
//            saveUser(admin);
//        }
//
//        Role userRole = roleDao.findByName(RoleType.USER);
//        if (userRole == null) {
//            userRole = new Role();
//            userRole.setName(RoleType.USER);
//            roleDao.save(userRole);
//        }
//    }
}
