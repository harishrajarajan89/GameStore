package com.gamestore.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gamestore.model.Cart;
import com.gamestore.model.User;
import com.gamestore.repository.CartRepository;
import com.gamestore.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository,
        CartRepository cartRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //  default test user 
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.USER)
                .build();
            testUser = userRepository.save(testUser);
            cartRepository.save(Cart.builder().user(testUser).build());
            System.out.println("✓ Default test user created: username=testuser, password=password123");
        }

        // admin user
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .build();
            adminUser = userRepository.save(adminUser);
            cartRepository.save(Cart.builder().user(adminUser).build());
            System.out.println("✓ Default admin user created: username=admin, password=admin123");
        }
    }
}
