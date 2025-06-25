package org.example.sklep_internetowy.service;

import org.example.sklep_internetowy.dto.RegisterRequest;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Zamiast własnego hashowania!

    // DODAJ - Spring Security wymaga tej metody
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // POPRAW - użyj RegisterRequest zamiast User
    public boolean register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return false;
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt zamiast SHA-256!
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        
        userRepository.save(user);
        return true;
    }

    // USUŃ - login będzie obsługiwany przez AuthenticationManager
    // public User login(String username, String password) { ... }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRoleOrderByUsername(role);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword)); // BCrypt!
        userRepository.save(user);
    }

    public boolean changePassword(User user, String currentPassword, String newPassword) {
        if (passwordEncoder.matches(currentPassword, user.getPassword())) { // matches zamiast equals!
            updatePassword(user, newPassword);
            return true;
        }
        return false;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public long getUsersCount() {
        return userRepository.count();
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // USUŃ - używamy BCrypt z Spring Security
    // private String hashPassword(String password) { ... }
}