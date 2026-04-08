package com.gamestore.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

//previously used user as a table name it caused issue
@Entity
@Table(name = "users")
//lombok is not working with java25 so switching back with java 22
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String username;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    @Default
    private Role role = Role.USER;
    public enum Role{
        USER,ADMIN
    }
    //without Default causing an error
    @Column
    @Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    @OneToMany(mappedBy = "user", cascade =CascadeType.ALL)
    private List<Order> orders;
}
