package org.example.sklep_internetowy.controller;

import org.example.sklep_internetowy.model.Order;
import org.example.sklep_internetowy.model.User;
import org.example.sklep_internetowy.service.OrderService;
import org.example.sklep_internetowy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderViewController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", 1); // This should be calculated based on actual pagination

        return "order/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order/details";
    }

    @GetMapping("/user/{userId}")
    public String listUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        User user = userService.getById(userId);
        if (user == null) {
            return "redirect:/orders";
        }

        List<Order> orders = orderService.getOrdersByUserId(userId);

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", 1); // This should be calculated based on actual pagination

        return "order/user-orders";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("isNew", true);
        return "order/form";
    }

    @GetMapping("/{id}/edit")
    public String editOrderForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("isNew", false);
        return "order/form";
    }

    @PostMapping
    public String saveOrder(@ModelAttribute Order order) {
        if (order.getId() == null) {
            orderService.createOrder(order);
        } else {
            orderService.updateOrder(order);
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order.Status orderStatus = Order.Status.valueOf(status.toUpperCase());
            orderService.updateOrderStatus(id, orderStatus);
        } catch (IllegalArgumentException e) {
            // Handle invalid status
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}
