package org.example.sklep_internetowy.service;

import org.example.sklep_internetowy.repository.OrderProductRepository;
import org.example.sklep_internetowy.repository.ProductRepository;
import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderProductService {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createOrderProduct(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    public OrderProduct getOrderProductById(Long id) {
        return orderProductRepository.findById(id).orElse(null);
    }

    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    public List<OrderProduct> getOrderProductsByOrder(Order order) {
        return orderProductRepository.findByOrder(order);
    }

    public List<OrderProduct> getOrderProductsByOrderId(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    public List<OrderProduct> getOrderProductsByProduct(Product product) {
        return orderProductRepository.findByProduct(product);
    }

    public List<OrderProduct> getOrderProductsByProductId(Long productId) {
        return orderProductRepository.findByProductId(productId);
    }

    public void updateOrderProduct(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProduct(Long id) {
        orderProductRepository.deleteById(id);
    }

    public void deleteOrderProductsByOrder(Order order) {
        orderProductRepository.deleteByOrder(order);
    }

    public void deleteOrderProductsByOrderId(Long orderId) {
        orderProductRepository.deleteByOrderId(orderId);
    }

    public long countOrderProductsByOrder(Long orderId) {
        return orderProductRepository.countByOrderId(orderId);
    }

    public double getTotalAmountByOrder(Long orderId) {
        Double total = orderProductRepository.getTotalAmountByOrderId(orderId);
        return total != null ? total : 0.0;
    }

    public int getTotalQuantityByProduct(Long productId) {
        Integer quantity = orderProductRepository.getTotalQuantityByProductId(productId);
        return quantity != null ? quantity : 0;
    }
}