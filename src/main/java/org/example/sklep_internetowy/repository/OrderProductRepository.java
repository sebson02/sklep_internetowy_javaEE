package org.example.sklep_internetowy.repository;

import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    
    List<OrderProduct> findByOrder(Order order);
    
    List<OrderProduct> findByOrderId(Long orderId);
    
    List<OrderProduct> findByProduct(Product product);
    
    List<OrderProduct> findByProductId(Long productId);
    
    void deleteByOrder(Order order);
    
    void deleteByOrderId(Long orderId);
    
    @Query("SELECT COUNT(op) FROM OrderProduct op WHERE op.order.id = :orderId")
    long countByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(op.quantity * op.product.price) FROM OrderProduct op WHERE op.order.id = :orderId")
    Double getTotalAmountByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(op.quantity) FROM OrderProduct op WHERE op.product.id = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);
}