package com.gamestore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gamestore.exception.BadRequestException;
import com.gamestore.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import com.gamestore.dto.CartItemRequest;
import com.gamestore.dto.CartItemResponse;
import com.gamestore.dto.CartResponse;
import com.gamestore.model.Cart;
import com.gamestore.model.CartItem;
import com.gamestore.model.Game;
import com.gamestore.model.User;
import com.gamestore.repository.CartItemRepository;
import com.gamestore.repository.CartRepository;
import com.gamestore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final GameService gameService;
    private final UserRepository userRepository;
    public CartService(CartRepository cartRepository,CartItemRepository cartItemRepository,GameService gameService,UserRepository userRepository){
        this.cartRepository=cartRepository;
        this.cartItemRepository=cartItemRepository;
        this.gameService=gameService;
        this.userRepository = userRepository;
    }
    public Cart getOrCreateCart(User user){
        return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            User managedUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found: " + user.getId()));
            Cart newCart = Cart.builder().user(managedUser).build();
            return cartRepository.save(newCart);
        });
    }
    public CartResponse getCart(User user){
        Cart cart = getOrCreateCart(user);
        return toResponse(cart);
    }
    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse>items= cart.getItems().stream().map(this::toItemResponse).collect(Collectors.toList());
        BigDecimal total =items.stream().map(CartItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        CartResponse res = new CartResponse();
        res.setId(cart.getId());
        res.setItems(items);
        res.setTotalAmount(total);
        return res;
    }
    private CartItemResponse toItemResponse(CartItem item) {
        CartItemResponse res = new CartItemResponse();
        res.setId(item.getId());
        res.setGameId(item.getGame().getId());
        res.setGameTitle(item.getGame().getTitle());
        res.setGameImageUrl(item.getGame().getImageUrl());
        res.setUnitPrice(item.getGame().getPrice());
        res.setQuantity(item.getQuantity());
        res.setSubtotal(item.getGame().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return res;
    }
    @Transactional
    public CartResponse addToCart(User user,CartItemRequest request){
        Cart cart = getOrCreateCart(user);
        Game game= gameService.getGameEntityById(request.getGameId());

        if(game.getStock()<request.getQuantity()){
            throw new BadRequestException("Insufficient quantity " + game.getTitle());
        }

        Optional<CartItem>existing = cartItemRepository.findByCartAndGame(cart, game);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int freshQuantity = item.getQuantity() + request.getQuantity();
            if (game.getStock() < freshQuantity) {
                throw new BadRequestException("Insufficient quantity " + game.getTitle());
            }
            item.setQuantity(freshQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                .cart(cart)
                .game(game)
                .quantity(request.getQuantity())
                .build();
            cartItemRepository.save(item);
        }

        Cart refreshedCart = cartRepository.findById(cart.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cart.getId()));
        return toResponse(refreshedCart);
    }
    @Transactional
    public CartResponse updaCartItem(User user, Long id, Integer qnt){
        Cart cart=getOrCreateCart(user);
        CartItem item =cartItemRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Item not found"));
        if(!Objects.equals(item.getCart().getId(), cart.getId())){
            throw new BadRequestException("Item not found in cart");
        }
        if(qnt <= 0){
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            if(item.getGame().getStock()<qnt){
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(qnt);
            cartItemRepository.save(item);
        }
        return toResponse(getOrCreateCart(user));
    }
    @Transactional
    public void clearCart(User user){
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse removeFromCart(User user, Long itemId) {
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!Objects.equals(item.getCart().getId(), cart.getId())) {
            throw new BadRequestException("Item does not belong to your cart");
        }
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        return toResponse(cartRepository.findById(cart.getId()).orElse(cart));
    }
}
