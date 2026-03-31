package com.gamestore.controller;

import org.apache.http.auth.AuthSchemeRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gamestore.dto.AuthenticationRequest;
import com.gamestore.dto.AuthenticationResponse;
import com.gamestore.dto.RegisterRequest;
import com.gamestore.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService=authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse>register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>login(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        HttpSession ses = request.getSession(false);
        if (ses!=null) {
            ses.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponse> getCurrentUser() {
        var user = authenticationService.getCurrentUser();
        return ResponseEntity.ok(new AuthenticationResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
    }


}
//trdyded yteuyr 