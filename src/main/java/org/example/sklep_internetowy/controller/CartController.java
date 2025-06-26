package org.example.sklep_internetowy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.example.sklep_internetowy.service.ProductService;
import org.example.sklep_internetowy.service.CartService;
import org.example.sklep_internetowy.model.Product;
import org.example.sklep_internetowy.model.OrderProduct;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import java.util.List;
import java.util.ArrayList;



@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Produkt nie istnieje!");
            return "redirect:/products";
        }

        // Pobierz koszyk z sesji (lista OrderProduct)
        List<OrderProduct> cart = (List<OrderProduct>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Sprawdź, czy produkt już jest w koszyku
        boolean found = false;
        for (OrderProduct op : cart) {
            if (op.getProduct().getId().equals(productId)) {
                op.setQuantity(op.getQuantity() + 1); // dodaj ilość
                found = true;
                break;
            }
        }
        if (!found) {
            OrderProduct newItem = new OrderProduct();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cart.add(newItem);
        }

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("success", "Produkt dodany do koszyka!");
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        List<OrderProduct> cart = (List<OrderProduct>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cartItems", cart);
        return "cart/view"; // widok z Twoim Thymeleaf
    }
}



