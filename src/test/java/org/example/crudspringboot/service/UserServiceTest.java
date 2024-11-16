package org.example.crudspringboot.service;

import org.example.crudspringboot.dao.UserDao;
import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role userRole = new Role(1L, Role.RoleType.USER);
        Set<Role> roles = Set.of(userRole);
        testUser = new User(1L, "testUser", "testLastname", "test@example.com", roles);
    }

    @Test
    void saveUser_ShouldInvokeSaveMethodInDao() {
        doNothing().when(userDao).save(any(User.class));

        userService.saveUser(testUser);

        verify(userDao, times(1)).save(testUser);
    }

    @Test
    void updateUserRoles_ShouldUpdateUserRoles() {
        Long userId = 1L;
        Set<Role> newRoles = Set.of(new Role(2L, Role.RoleType.ADMIN));

        when(userDao.findById(userId)).thenReturn(testUser);

        userService.updateUserRoles(userId, newRoles);

        verify(userDao, times(1)).save(testUser);
        assertEquals(newRoles, testUser.getRoles());
    }

    @Test
    void deleteUser_ShouldInvokeDeleteMethodInDao() {
        doNothing().when(userDao).delete(any(User.class));

        userService.deleteUser(testUser);

        verify(userDao, times(1)).delete(testUser);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        Long userId = 1L;
        when(userDao.findById(userId)).thenReturn(testUser);

        User foundUser = userService.getUserById(userId);

        assertEquals(testUser, foundUser);
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = List.of(testUser, new User(2L, "testUser2", "testLastname2", "test2@example.com", Set.of()));

        when(userDao.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertEquals(users, foundUsers);
        verify(userDao, times(1)).findAll();
    }

    @Test
    void getUserByUsername_ShouldReturnUser() {
        String username = "testUser";
        when(userDao.findByUsername(username)).thenReturn(testUser);

        User foundUser = userService.getUserByUsername(username);

        assertEquals(testUser, foundUser);
        verify(userDao, times(1)).findByUsername(username);
    }

    @Test
    void updateUser_ShouldUpdateOnlyChangedFields() {
        User oldUser = new User(1L, "oldUsername", "oldLastname", "old@example.com", Set.of(new Role(1L, Role.RoleType.USER)));
        when(userDao.findById(testUser.getId())).thenReturn(oldUser);
        doNothing().when(userDao).save(any(User.class));

        userService.updateUser(testUser);

        verify(userDao, times(1)).findById(testUser.getId());
        verify(userDao, times(1)).save(testUser);
        assertEquals("testUser", testUser.getUsername());
        assertEquals("testLastname", testUser.getLastname());
        assertEquals("test@example.com", testUser.getEmail());
    }
}