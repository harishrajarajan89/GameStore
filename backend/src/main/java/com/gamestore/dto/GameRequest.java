package com.gamestore.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private BigDecimal price;
    private String image;
    private String genre;
    private String platform;

    @Min(0)
    private Integer stock =0;
}
