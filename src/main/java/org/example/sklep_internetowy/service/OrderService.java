package org.example.sklep_internetowy.service;

import org.example.sklep_internetowy.repository.OrderRepository;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(Order.Status status) {
        return orderRepository.findByStatus(status);
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public long getOrdersCount() {
        return orderRepository.count();
    }

    public long getOrdersCountByUser(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    public void updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}