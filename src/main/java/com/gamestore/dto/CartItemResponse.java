package com.gamestore.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class CartItemResponse {
    private Long id;
    private Long gameId;
    private String gameTitle;
    private String gameImageUrl;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}