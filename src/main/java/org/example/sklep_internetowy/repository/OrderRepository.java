package org.example.sklep_internetowy.repository;

import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByUserId(Long userId);
    
    List<Order> findByStatus(Order.Status status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}