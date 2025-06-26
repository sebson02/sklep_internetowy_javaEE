package org.example.sklep_internetowy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.sklep_internetowy.model.User;

public class RegisterRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String firstName;
    private String lastName;
    private User.Role role = User.Role.CUSTOMER;
    private Long id;

    public RegisterRequest(String username, String pw, String mail, String firstName, String surname) {
            this.username = username;
            this.password = pw;
            this.email = mail;
            this.firstName = firstName;
            this.lastName = surname;
            this.role = User.Role.CUSTOMER;
    }

    // Gettery i settery
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
}