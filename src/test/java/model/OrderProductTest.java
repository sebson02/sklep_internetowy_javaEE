package model;

import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductTest {

    private Order order;
    private Product product;
    private OrderProduct orderProduct;

    @BeforeEach
    void setUp() {
        order = new Order();
        product = new Product();
        product.setPrice(49.99);

        orderProduct = new OrderProduct(order, product, 2);
    }

    @Test
    void testConstructorInitializesFieldsCorrectly() {
        assertThat(orderProduct.getOrder()).isEqualTo(order);
        assertThat(orderProduct.getProduct()).isEqualTo(product);
        assertThat(orderProduct.getQuantity()).isEqualTo(2);
    }

    @Test
    void testGetTotalPriceCalculatesCorrectly() {
        double expected = 49.99 * 2;
        assertThat(orderProduct.getTotalPrice()).isEqualTo(expected);
    }

    @Test
    void testSettersAndGetters() {
        Order newOrder = new Order();
        Product newProduct = new Product();
        newProduct.setPrice(100.0);

        orderProduct.setOrder(newOrder);
        orderProduct.setProduct(newProduct);
        orderProduct.setQuantity(3);

        assertThat(orderProduct.getOrder()).isEqualTo(newOrder);
        assertThat(orderProduct.getProduct()).isEqualTo(newProduct);
        assertThat(orderProduct.getQuantity()).isEqualTo(3);
        assertThat(orderProduct.getTotalPrice()).isEqualTo(300.0);
    }

    @Test
    void testIdGetterAndSetter() {
        orderProduct.setId(42L);
        assertThat(orderProduct.getId()).isEqualTo(42L);
    }
}

