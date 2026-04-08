package com.gamestore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long gameId;
    private String gameTitle;
    private String gameImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
