package org.example.sklep_internetowy.dto;

import org.example.sklep_internetowy.model.User;

public class UserResponse {
    private Long id;
    private String username;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name();
    }

    // Gettery
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
