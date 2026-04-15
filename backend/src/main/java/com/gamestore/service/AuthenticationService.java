package com.gamestore.service;

import com.gamestore.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.gamestore.model.Cart;
import com.gamestore.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamestore.dto.AuthenticationRequest;
import com.gamestore.dto.AuthenticationResponse;
import com.gamestore.dto.RegisterRequest;
import com.gamestore.repository.CartRepository;
import com.gamestore.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthenticationService(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository=userRepository;
        this.cartRepository=cartRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
    }
    @Transactional
    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("Username already exists");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        User user = User.builder().username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode((request.getPassword()))).role(User.Role.USER).build();
        user = userRepository.save(user);
        Cart cart = Cart.builder().user(user).build();
        cartRepository.save(cart);
        return new AuthenticationResponse(user.getId(),user.getUsername(),user.getEmail(),user.getRole().name());
    }

    public AuthenticationResponse login (AuthenticationRequest request){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        return new AuthenticationResponse(user.getId(),user.getUsername(),user.getEmail(),user.getRole().name());
    }
    public User getCurrentUser(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(()->new BadRequestException("Not found "+username));
    }
}
