package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sklep_internetowy.controller.ProductController;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Product getSampleProduct() {
        return new Product("Laptop", "Gaming laptop", 2999.99, 10);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(getSampleProduct()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Product product = getSampleProduct();
        Mockito.when(productService.getProduct(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        Mockito.when(productService.getProduct(1L)).thenReturn(null);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddProduct() throws Exception {
        Product product = getSampleProduct();
        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product added successfully"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product product = getSampleProduct();
        product.setId(1L);
        Mockito.when(productService.getProduct(1L)).thenReturn(product);

        String updatedJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        Mockito.when(productService.getProduct(1L)).thenReturn(null);

        String json = objectMapper.writeValueAsString(getSampleProduct());

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = getSampleProduct();
        Mockito.when(productService.getProduct(1L)).thenReturn(product);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentProduct() throws Exception {
        Mockito.when(productService.getProduct(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound());
    }
}

