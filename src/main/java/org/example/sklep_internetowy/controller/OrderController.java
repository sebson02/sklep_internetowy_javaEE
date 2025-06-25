package org.example.sklep_internetowy.controller;

import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.OrderService;
import org.example.sklep_internetowy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            Order.Status orderStatus = Order.Status.valueOf(status.toUpperCase());
            List<Order> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }
        updatedOrder.setId(id);
        orderService.updateOrder(updatedOrder);
        return ResponseEntity.ok("Order updated successfully");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order.Status orderStatus = Order.Status.valueOf(status.toUpperCase());
            orderService.updateOrderStatus(id, orderStatus);
            return ResponseEntity.ok("Order status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getOrdersCount() {
        long count = orderService.getOrdersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> getOrdersCountByUser(@PathVariable Long userId) {
        long count = orderService.getOrdersCountByUser(userId);
        return ResponseEntity.ok(count);
    }
}