package org.example.sklep_internetowy.controller;

import jakarta.validation.Valid;
import org.example.sklep_internetowy.dto.AuthResponse;
import org.example.sklep_internetowy.dto.LoginRequest;
import org.example.sklep_internetowy.dto.RegisterRequest;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.JwtService;
import org.example.sklep_internetowy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Zmiana ścieżki na /api/auth
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // DODAJ

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService; // DODAJ

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) { // Zmiana na RegisterRequest
        try {
            boolean created = userService.register(request);
            if (!created) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }

            // DODAJ - automatyczne logowanie po rejestracji z tokenem JWT
            User user = userService.getByUsername(request.getUsername());
            String jwt = jwtService.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(jwt, user.getId(), user.getUsername(), 
                                         user.getEmail(), user.getRole()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // First try to authenticate with Spring Security (BCrypt)
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword())
                );

                User user = (User) authentication.getPrincipal();
                String jwt = jwtService.generateToken(user);

                return ResponseEntity.ok(new AuthResponse(jwt, user.getId(), user.getUsername(), 
                                                         user.getEmail(), user.getRole()));
            } catch (AuthenticationException e) {
                // If BCrypt authentication fails, try direct password check
                User user = userService.getByUsername(request.getUsername());
                if (user != null && user.getPassword().equals(request.getPassword())) {
                    // If direct comparison works, update password to BCrypt
                    userService.updatePassword(user, request.getPassword());

                    // Now authenticate with the updated password
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword())
                    );

                    String jwt = jwtService.generateToken(user);
                    return ResponseEntity.ok(new AuthResponse(jwt, user.getId(), user.getUsername(), 
                                                             user.getEmail(), user.getRole()));
                }

                // If both authentication methods fail
                throw e;
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // DODAJ - endpoint do sprawdzania ważności tokena
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                String username = jwtService.extractUsername(jwt);
                User user = userService.getByUsername(username);

                if (jwtService.validateToken(jwt, user)) {
                    return ResponseEntity.ok("Token is valid");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }
}
