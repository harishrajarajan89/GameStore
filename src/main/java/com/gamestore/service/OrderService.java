package com.gamestore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gamestore.dto.OrderItemResponse;
import com.gamestore.dto.OrderResponse;
import com.gamestore.exception.BadRequestException;
import com.gamestore.exception.ResourceNotFoundException;
import com.gamestore.model.Cart;
import com.gamestore.model.CartItem;
import com.gamestore.model.Game;
import com.gamestore.model.Order;
import com.gamestore.model.OrderItem;
import com.gamestore.model.User;
import com.gamestore.repository.CartItemRepository;
import com.gamestore.repository.CartRepository;
import com.gamestore.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    public OrderService(OrderRepository orderRepository,CartRepository cartRepository,CartItemRepository cartItemRepository){
        this.orderRepository=orderRepository;
        this.cartItemRepository=cartItemRepository;
        this.cartRepository=cartRepository;
    }
    @Transactional
    public OrderResponse checkout(User userr){
        Cart cart = cartRepository.findByUser(userr).orElseThrow(()-> new BadRequestException("Cart not available"));
        if(cart.getItems().isEmpty()){
            throw new BadRequestException("Empty Cart");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Game game = cartItem.getGame();
            if (game.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock : " + game.getTitle());
            }
            game.setStock(game.getStock() - cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder().game(game).quantity(cartItem.getQuantity()).unitPrice(game.getPrice()).build();
            orderItems.add(orderItem);
            total = total.add(game.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        Order order = Order.builder().user(userr).totalAmount(total).status(Order.OrderStatus.PENDING).build();
        Order savedOrder = orderRepository.save(order);
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }
        savedOrder.setItems(orderItems);
        orderRepository.save(savedOrder);
        cart.getItems().clear();
        cartRepository.save(cart);
        return toResponse(savedOrder);
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setUserId(order.getUser().getId());
        res.setUsername(order.getUser().getUsername());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus());
        res.setCreatedAt(order.getCreatedAt());
        List<OrderItemResponse> items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemResponse ir = new OrderItemResponse();
            ir.setId(item.getId());
            ir.setGameId(item.getGame().getId());
            ir.setGameTitle(item.getGame().getTitle());
            ir.setQuantity(item.getQuantity());
            ir.setUnitPrice(item.getUnitPrice());
            ir.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            items.add(ir);
        }
        res.setItems(items);
        return res;
    }
    public List<OrderResponse> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public OrderResponse getOrderById(Long id, User user) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // to allow admin to view
        if (!order.getUser().getId().equals(user.getId()) &&user.getRole() != User.Role.ADMIN) {
            throw new BadRequestException("Access denied");
        }
        return toResponse(order);
    }
    // to allow admin to get all orders
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        try {
            order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        } 
        catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }
        return toResponse(orderRepository.save(order));
    }
}
