package org.example.sklep_internetowy.repository;

import org.example.sklep_internetowy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email); // DODAJ
    
    List<User> findByRoleOrderByUsername(User.Role role);
    
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email); // DODAJ
}