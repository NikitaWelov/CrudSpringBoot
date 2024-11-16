package org.example.crudspringboot.service;

import jakarta.transaction.Transactional;
import org.example.crudspringboot.dao.UserDao;
import org.example.crudspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void saveUser(User user) {
        //TODO: При создании пользователя устанавливаем роль по умолчанию (USER)
        userDao.save(user);
    }

    // TODO: Добавить метод updateUserRoles(Long userId, Set<Role> roles) который будет обновлять роль у пользователя

    @Transactional
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    @Transactional
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    @Transactional
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    public void updateUser(User user) {
        User oldUser = userDao.findById(user.getId());

        if (!oldUser.getUsername().equals(user.getUsername())) {
            user.setUsername(user.getUsername());
        }
        if (!oldUser.getLastname().equals(user.getLastname())) {
            user.setLastname(user.getLastname());
        }
        if (!oldUser.getEmail().equals(user.getEmail())) {
            user.setEmail(user.getEmail());
        }
    }
}
