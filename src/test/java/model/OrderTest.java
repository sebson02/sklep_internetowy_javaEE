package model;

import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Order order = new Order();
        User user = new User("jan", "password", "jan@example.com", "Jan", "Kowalski", User.Role.CUSTOMER);
        LocalDateTime now = LocalDateTime.now();

        order.setId(1L);
        order.setUser(user);
        order.setStatus(Order.Status.CONFIRMED);
        order.setCreatedAt(now);

        OrderProduct op = new OrderProduct(order, new Product("Phone", "Smartphone", 999.0, 2), 1);
        order.setOrderProducts(List.of(op));

        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getStatus()).isEqualTo(Order.Status.CONFIRMED);
        assertThat(order.getCreatedAt()).isEqualTo(now);
        assertThat(order.getOrderProducts()).hasSize(1).contains(op);
    }

    @Test
    void testConstructorWithUser() {
        User user = new User("ania", "secret123", "ania@example.com", "Ania", "Nowak", User.Role.CUSTOMER);
        Order order = new Order(user);

        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getStatus()).isEqualTo(Order.Status.PENDING);
        assertThat(order.getCreatedAt()).isNotNull();
    }
}
