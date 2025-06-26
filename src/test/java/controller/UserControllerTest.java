package controller;

import org.example.sklep_internetowy.controller.UserController;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pass");
        user.setRole(User.Role.CUSTOMER);
        return user;
    }

    @Test
    void shouldGetUserById() throws Exception {
        when(userService.getById(1L)).thenReturn(createUser());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void shouldReturnNotFoundWhenUserByIdDoesNotExist() throws Exception {
        when(userService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetUserByUsername() throws Exception {
        when(userService.getByUsername("john")).thenReturn(createUser());

        mockMvc.perform(get("/api/users/username/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(createUser()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User updatedUser = createUser();
        updatedUser.setUsername("updated");

        when(userService.getById(1L)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"username\":\"updated\",\"password\":\"pass\",\"role\":\"USER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUserRole() throws Exception {
        User user = createUser();
        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(put("/api/users/1/role?role=ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("User role updated successfully"));
    }

    @Test
    void shouldReturnBadRequestOnInvalidRole() throws Exception {
        User user = createUser();
        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(put("/api/users/1/role?role=INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid role"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        when(userService.getById(1L)).thenReturn(createUser());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void shouldChangePassword() throws Exception {
        User user = createUser();
        when(userService.getById(1L)).thenReturn(user);
        when(userService.changePassword(user, "oldPass", "newPass")).thenReturn(true);

        mockMvc.perform(put("/api/users/1/password?currentPassword=oldPass&newPassword=newPass"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIncorrect() throws Exception {
        User user = createUser();
        when(userService.getById(1L)).thenReturn(user);
        when(userService.changePassword(user, "wrong", "newPass")).thenReturn(false);

        mockMvc.perform(put("/api/users/1/password?currentPassword=wrong&newPassword=newPass"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Current password is incorrect"));
    }

    @Test
    void shouldReturnUserCount() throws Exception {
        when(userService.getUsersCount()).thenReturn(5L);

        mockMvc.perform(get("/api/users/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void shouldGetUsersByRole() throws Exception {
        when(userService.getUsersByRole(User.Role.CUSTOMER)).thenReturn(List.of(createUser()));

        mockMvc.perform(get("/api/users/role/USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john"));
    }
}

