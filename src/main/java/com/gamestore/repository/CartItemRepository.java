package com.gamestore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamestore.model.Cart;
import com.gamestore.model.CartItem;
import com.gamestore.model.Game;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndGame(Cart cart, Game game);
    void deleteByCartId(Long cartId);
}
