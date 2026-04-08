package com.gamestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CartItemRequest {
    @NotNull(message = "gameId is required")
    private Long gameId;
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity=1;
}
