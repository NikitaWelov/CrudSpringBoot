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
        userDao.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }


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
