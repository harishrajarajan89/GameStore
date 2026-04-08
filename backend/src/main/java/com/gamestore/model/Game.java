package com.gamestore.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
@Entity
@Table(name = "games")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    @Column(length = 50)
    private String genre;
    @Column(length = 50)
    private String platform;
    @Column(nullable = false)
    @Default
    private Integer stock = 0;
    @Column(name = "created_at")
    @Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    @Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
