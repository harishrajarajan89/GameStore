package com.gamestore.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.gamestore.model.Order;
import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String username;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
}
