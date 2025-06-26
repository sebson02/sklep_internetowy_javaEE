package org.example.sklep_internetowy.service;

import org.example.sklep_internetowy.model.Product;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    public List<Product> getCart(HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product) {
        List<Product> cart = getCart(session);
        cart.add(product);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}

