package com.gamestore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gamestore.model.Cart;
import com.gamestore.model.User;
import com.gamestore.repository.CartRepository;
import com.gamestore.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.username:}")
    private String adminUsername;

    @Value("${app.seed.admin.email:}")
    private String adminEmail;

    @Value("${app.seed.admin.password:}")
    private String adminPassword;

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
    public void run(String... args) {
        if (adminUsername.isBlank() || adminEmail.isBlank() || adminPassword.isBlank()) {
            logger.info("Admin seed skipped because app.seed.admin.* values were not fully provided.");
            return;
        }

        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        User adminUser = User.builder()
            .username(adminUsername)
            .email(adminEmail)
            .password(passwordEncoder.encode(adminPassword))
            .role(User.Role.ADMIN)
            .build();
        adminUser = userRepository.save(adminUser);
        cartRepository.save(Cart.builder().user(adminUser).build());
        logger.info("Default admin user created for {}", adminUsername);
    }
}
