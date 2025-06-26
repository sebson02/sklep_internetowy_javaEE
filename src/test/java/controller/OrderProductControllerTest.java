package controller;

import org.example.sklep_internetowy.controller.OrderProductController;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.OrderProduct;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.service.OrderProductService;
import org.example.sklep_internetowy.service.OrderService;
import org.example.sklep_internetowy.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderProductController.class)
class OrderProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderProductService orderProductService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnAllOrderProducts() throws Exception {
        Mockito.when(orderProductService.getAllOrderProducts()).thenReturn(List.of());
        mockMvc.perform(get("/api/order-products"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOrderProductById() throws Exception {
        OrderProduct op = new OrderProduct(new Order(), new Product(), 2);
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(op);
        mockMvc.perform(get("/api/order-products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundForMissingOrderProduct() throws Exception {
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/order-products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOrderProductsByOrderId() throws Exception {
        Mockito.when(orderProductService.getOrderProductsByOrderId(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/order-products/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOrderProductsByProductId() throws Exception {
        Mockito.when(orderProductService.getOrderProductsByProductId(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/order-products/product/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateOrderProduct() throws Exception {
        String json = """
                {
                  "quantity": 1,
                  "order": {"id": 1},
                  "product": {"id": 1}
                }
                """;
        mockMvc.perform(post("/api/order-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateOrderProduct() throws Exception {
        OrderProduct existing = new OrderProduct(new Order(), new Product(), 1);
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(existing);
        String json = """
                {
                  "quantity": 3,
                  "order": {"id": 1},
                  "product": {"id": 1}
                }
                """;
        mockMvc.perform(put("/api/order-products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingOrderProduct() throws Exception {
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(null);
        String json = "{}";
        mockMvc.perform(put("/api/order-products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteOrderProduct() throws Exception {
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(new OrderProduct(new Order(), new Product(), 1));
        mockMvc.perform(delete("/api/order-products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingOrderProduct() throws Exception {
        Mockito.when(orderProductService.getOrderProductById(1L)).thenReturn(null);
        mockMvc.perform(delete("/api/order-products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteOrderProductsByOrderId() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(new Order());
        mockMvc.perform(delete("/api/order-products/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingByInvalidOrderId() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(null);
        mockMvc.perform(delete("/api/order-products/order/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOrderTotalAmount() throws Exception {
        Mockito.when(orderProductService.getTotalAmountByOrder(anyLong())).thenReturn(250.0);
        mockMvc.perform(get("/api/order-products/order/1/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(250.0));
    }
}

