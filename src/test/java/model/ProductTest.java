package model;

import org.example.sklep_internetowy.model.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Product product = new Product();
        LocalDateTime now = LocalDateTime.now();

        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(2999.99);
        product.setStockQuantity(10);
        product.setCreatedAt(now);

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Laptop");
        assertThat(product.getDescription()).isEqualTo("High-performance laptop");
        assertThat(product.getPrice()).isEqualTo(2999.99);
        assertThat(product.getStockQuantity()).isEqualTo(10);
        assertThat(product.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testConstructorWithArguments() {
        Product product = new Product("Smartphone", "Android device", 1999.99, 5);

        assertThat(product.getName()).isEqualTo("Smartphone");
        assertThat(product.getDescription()).isEqualTo("Android device");
        assertThat(product.getPrice()).isEqualTo(1999.99);
        assertThat(product.getStockQuantity()).isEqualTo(5);
        assertThat(product.getCreatedAt()).isNotNull();
    }

    @Test
    void testToStringReturnsExpectedFormat() {
        Product product = new Product("TV", "Smart TV", 2999.0, 3);
        product.setId(42L);

        String toString = product.toString();
        assertThat(toString).contains("Product{");
        assertThat(toString).contains("id=42");
        assertThat(toString).contains("name='TV'");
        assertThat(toString).contains("price=2999.0");
        assertThat(toString).contains("stockQuantity=3");
    }
}
