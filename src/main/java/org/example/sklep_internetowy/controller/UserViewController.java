package org.example.sklep_internetowy.controller;

import org.example.sklep_internetowy.dto.RegisterRequest;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role,
            Model model) {
        
        List<User> users;
        if (role != null && !role.isEmpty()) {
            try {
                User.Role userRole = User.Role.valueOf(role.toUpperCase());
                users = userService.getUsersByRole(userRole);
            } catch (IllegalArgumentException e) {
                users = userService.getAllUsers();
            }
        } else {
            users = userService.getAllUsers();
        }
        
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", 1); // This should be calculated based on actual pagination
        model.addAttribute("roles", User.Role.values());
        
        return "user/list";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user/details";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        model.addAttribute("isNew", true);
        return "user/form";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", User.Role.values());
        model.addAttribute("isNew", false);
        return "user/form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute RegisterRequest registerRequest) {
        if (registerRequest.getId() == null) {
            userService.register(registerRequest);
        } else {
            // For updating existing users, we'd need to handle this differently
            // as RegisterRequest doesn't have an ID field
            User user = userService.getById(registerRequest.getId());
            if (user != null) {
                user.setUsername(registerRequest.getUsername());
                user.setEmail(registerRequest.getEmail());
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(registerRequest.getLastName());
                user.setRole(registerRequest.getRole());
                userService.updateUser(user);
            }
        }
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/{id}/change-password")
    public String changePasswordForm(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user/change-password";
    }

    @PostMapping("/{id}/change-password")
    public String changePassword(
            @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/users";
        }
        
        boolean success = userService.changePassword(user, currentPassword, newPassword);
        if (success) {
            return "redirect:/users/" + id + "?passwordChanged=true";
        } else {
            return "redirect:/users/" + id + "/change-password?error=true";
        }
    }
}