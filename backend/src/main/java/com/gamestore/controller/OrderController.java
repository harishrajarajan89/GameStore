package com.gamestore.controller;

import com.gamestore.dto.OrderResponse;
import com.gamestore.model.User;
import com.gamestore.service.AuthenticationService;
import com.gamestore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationService authService;
    public OrderController(OrderService orderService, AuthenticationService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(orderService.checkout(user));
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(orderService.getUserOrders(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(orderService.getOrderById(id, user));
    }
}
