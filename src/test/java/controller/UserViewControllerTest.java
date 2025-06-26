package controller;

import org.example.sklep_internetowy.controller.UserViewController;
import org.example.sklep_internetowy.dto.RegisterRequest;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserViewController.class)
class UserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User getUser() {
        User u = new User();
        u.setId(1L);
        u.setUsername("john");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setEmail("john@example.com");
        u.setRole(User.Role.CUSTOMER);
        return u;
    }

    @Test
    void shouldListAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(getUser()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"));
    }

    @Test
    void shouldViewUserDetails() throws Exception {
        when(userService.getById(1L)).thenReturn(getUser());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/details"));
    }

    @Test
    void shouldRedirectWhenUserNotFound() throws Exception {
        when(userService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/users/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void shouldRenderNewUserForm() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/form"));
    }

    @Test
    void shouldRenderEditUserForm() throws Exception {
        when(userService.getById(1L)).thenReturn(getUser());

        mockMvc.perform(get("/users/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/form"));
    }

    @Test
    void shouldRedirectOnEditFormWhenUserNotFound() throws Exception {
        when(userService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/users/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(post("/users/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).deleteUser(1L);
    }

    @Test
    void shouldShowChangePasswordForm() throws Exception {
        when(userService.getById(1L)).thenReturn(getUser());

        mockMvc.perform(get("/users/1/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/change-password"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRedirectWhenChangingPasswordForInvalidUser() throws Exception {
        when(userService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1/change-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void shouldRedirectAfterPasswordChangeSuccess() throws Exception {
        when(userService.getById(1L)).thenReturn(getUser());
        when(userService.changePassword(any(), eq("old"), eq("new"))).thenReturn(true);

        mockMvc.perform(post("/users/1/change-password")
                        .param("currentPassword", "old")
                        .param("newPassword", "new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1?passwordChanged=true"));
    }

    @Test
    void shouldRedirectAfterPasswordChangeFailure() throws Exception {
        when(userService.getById(1L)).thenReturn(getUser());
        when(userService.changePassword(any(), eq("old"), eq("new"))).thenReturn(false);

        mockMvc.perform(post("/users/1/change-password")
                        .param("currentPassword", "old")
                        .param("newPassword", "new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1/change-password?error=true"));
    }

    @Test
    void shouldSaveNewUser() throws Exception {
        mockMvc.perform(post("/users")
                        .param("username", "john")
                        .param("email", "john@example.com")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).register(ArgumentMatchers.any(RegisterRequest.class));
    }
}

