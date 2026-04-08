package com.gamestore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class GameResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String genre;
    private String platform;
    private Integer stock;
    private java.time.LocalDateTime createdAt;
}
