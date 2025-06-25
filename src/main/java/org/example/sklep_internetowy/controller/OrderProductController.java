package org.example.sklep_internetowy.controller;

import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.service.OrderProductService;
import org.example.sklep_internetowy.service.OrderService;
import org.example.sklep_internetowy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-products")
public class OrderProductController {

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<OrderProduct>> getAllOrderProducts() {
        List<OrderProduct> orderProducts = orderProductService.getAllOrderProducts();
        return ResponseEntity.ok(orderProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getOrderProduct(@PathVariable Long id) {
        OrderProduct orderProduct = orderProductService.getOrderProductById(id);
        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderProduct);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderProduct>> getOrderProductsByOrder(@PathVariable Long orderId) {
        List<OrderProduct> orderProducts = orderProductService.getOrderProductsByOrderId(orderId);
        return ResponseEntity.ok(orderProducts);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderProduct>> getOrderProductsByProduct(@PathVariable Long productId) {
        List<OrderProduct> orderProducts = orderProductService.getOrderProductsByProductId(productId);
        return ResponseEntity.ok(orderProducts);
    }

    @PostMapping
    public ResponseEntity<String> createOrderProduct(@RequestBody OrderProduct orderProduct) {
        orderProductService.createOrderProduct(orderProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order product created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderProduct(@PathVariable Long id, @RequestBody OrderProduct updatedOrderProduct) {
        OrderProduct existingOrderProduct = orderProductService.getOrderProductById(id);
        if (existingOrderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        updatedOrderProduct.setId(id);
        orderProductService.updateOrderProduct(updatedOrderProduct);
        return ResponseEntity.ok("Order product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderProduct(@PathVariable Long id) {
        OrderProduct orderProduct = orderProductService.getOrderProductById(id);
        if (orderProduct == null) {
            return ResponseEntity.notFound().build();
        }
        orderProductService.deleteOrderProduct(id);
        return ResponseEntity.ok("Order product deleted successfully");
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrderProductsByOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        orderProductService.deleteOrderProductsByOrderId(orderId);
        return ResponseEntity.ok("Order products deleted successfully");
    }

    @GetMapping("/order/{orderId}/total")
    public ResponseEntity<Map<String, Double>> getOrderTotal(@PathVariable Long orderId) {
        double total = orderProductService.getTotalAmountByOrder(orderId);
        return ResponseEntity.ok(Map.of("total", total));
    }
}