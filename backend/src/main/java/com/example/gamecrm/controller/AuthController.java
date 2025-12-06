package com.example.gamecrm.controller;

import com.example.gamecrm.model.Customer;
import com.example.gamecrm.repository.CustomerRepository;
import com.example.gamecrm.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final CustomerRepository repo;
    private final JwtUtil jwt;
    private final PasswordEncoder encoder;

    public AuthController(CustomerRepository repo, JwtUtil jwt, PasswordEncoder encoder) {
        this.repo = repo;
        this.jwt = jwt;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        Customer c = repo.findByCorreo(email).orElse(null);

        if (c == null || !encoder.matches(password, c.getPassword())) {
            return Map.of("error", "Invalid credentials");
        }

        String token = jwt.generateToken(email);

        return Map.of(
                "token", token,
                "email", email,
                "customerId", c.getId(),
                "isAdmin", c.isAdmin()
        );
    }
}
