package org.example.crudspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.crudspringboot.model.Role;
import org.example.crudspringboot.model.RoleType;
import org.example.crudspringboot.model.User;
import org.example.crudspringboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        Role userRole = new Role(1L, RoleType.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        testUser = new User(1L, "testUser", "testLastname", "test@email.com", roles);

        User testUser2 = new User(2L, "testUser2", "testLastname2", "test2@email.com", roles);
        testUsers = Arrays.asList(testUser, testUser2);
    }

    @Test
    void getUsersHTML_ShouldReturnUsersView() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("testUser"))
                .andExpect(jsonPath("$[1].username").value("testUser2"));
    }

    @Test
    void saveUser_ShouldReturnSavedUser() throws Exception {
        doNothing().when(userService).saveUser(any(User.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@email.com"));

        verify(userService).saveUser(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUser);
        doNothing().when(userService).deleteUser(any(User.class));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService).deleteUser(testUser);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnOk() throws Exception {
        when(userService.getUserById(999L)).thenReturn(null);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isOk());

        verify(userService, never()).deleteUser(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService).updateUser(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnOk() throws Exception {
        when(userService.getUserById(999L)).thenReturn(null);

        mockMvc.perform(put("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        verify(userService, never()).updateUser(any(User.class));
    }
}