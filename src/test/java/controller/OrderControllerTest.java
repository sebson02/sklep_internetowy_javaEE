package controller;
import org.example.sklep_internetowy.controller.OrderController;
import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.OrderService;
import org.example.sklep_internetowy.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private Order sampleOrder() {
        User user = new User("user1", "pass123", "email@example.com", "Jan", "Kowalski", User.Role.CUSTOMER);
        Order order = new Order(user);
        order.setId(1L);
        order.setStatus(Order.Status.PENDING);
        return order;
    }

    @Test
    @DisplayName("GET /api/orders - should return all orders")
    void shouldGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(sampleOrder()));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/orders/1 - should return specific order")
    void shouldGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrder());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/orders/999 - should return 404")
    void shouldReturnNotFoundForMissingOrder() throws Exception {
        when(orderService.getOrderById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/orders - should create order")
    void shouldCreateOrder() throws Exception {
        Order order = sampleOrder();
        String json = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order created successfully"));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    @DisplayName("PUT /api/orders/1 - should update order")
    void shouldUpdateOrder() throws Exception {
        Order updatedOrder = sampleOrder();
        updatedOrder.setStatus(Order.Status.CONFIRMED);

        when(orderService.getOrderById(1L)).thenReturn(sampleOrder());

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated successfully"));
    }

    @Test
    @DisplayName("PUT /api/orders/1/status?status=SHIPPED - should update status")
    void shouldUpdateOrderStatus() throws Exception {
        mockMvc.perform(put("/api/orders/1/status?status=SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated successfully"));

        verify(orderService).updateOrderStatus(1L, Order.Status.SHIPPED);
    }

    @Test
    @DisplayName("PUT /api/orders/1/status?status=INVALID - should return 400")
    void shouldReturnBadRequestForInvalidStatus() throws Exception {
        mockMvc.perform(put("/api/orders/1/status?status=INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid status"));
    }

    @Test
    @DisplayName("DELETE /api/orders/1 - should delete order")
    void shouldDeleteOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrder());

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully"));
    }

    @Test
    @DisplayName("GET /api/orders/count - should return count")
    void shouldGetOrdersCount() throws Exception {
        when(orderService.getOrdersCount()).thenReturn(5L);

        mockMvc.perform(get("/api/orders/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @DisplayName("GET /api/orders/count/user/1 - should return user order count")
    void shouldGetOrdersCountByUser() throws Exception {
        when(orderService.getOrdersCountByUser(1L)).thenReturn(3L);

        mockMvc.perform(get("/api/orders/count/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    @DisplayName("GET /api/orders/status/pending - should return orders by status")
    void shouldGetOrdersByStatus() throws Exception {
        when(orderService.getOrdersByStatus(Order.Status.PENDING)).thenReturn(List.of(sampleOrder()));

        mockMvc.perform(get("/api/orders/status/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}

