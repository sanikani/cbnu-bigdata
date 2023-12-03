package com.cbnu.shop.controller;

import com.cbnu.shop.repository.CartRepository;
import com.cbnu.shop.repository.OrderItemRepository;
import com.cbnu.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class JdbcController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @GetMapping("/insert/cart-item")
    public String insertCartItem() {
        cartRepository.insertRandomCartItems();
        return "cart";
    }

    @GetMapping("/insert/order")
    public String insertOrder() {
        orderRepository.insertRandomOrders();
        return "order";
    }

    @GetMapping("/insert/order-item")
    public String insertOrderItem() {
        orderItemRepository.insertIntoRandomOrderItems();
        return "order";
    }
}
