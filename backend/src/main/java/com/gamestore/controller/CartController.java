package com.gamestore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gamestore.dto.CartItemRequest;
import com.gamestore.dto.CartResponse;
import com.gamestore.model.User;
import com.gamestore.service.AuthenticationService;
import com.gamestore.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final AuthenticationService authService;
    public CartController(CartService cartService, AuthenticationService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.getCart(user));
    }
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartItemRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.addToCart(user, request));
    }
    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long id,@RequestParam Integer quantity) {
        User user=authService.getCurrentUser();
        return ResponseEntity.ok(cartService.updaCartItem(user, id, quantity));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.removeFromCart(user, id));
    }
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        User user=authService.getCurrentUser();
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
