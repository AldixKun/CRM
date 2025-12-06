package com.example.gamecrm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.gamecrm.model.Customer;
import com.example.gamecrm.repository.CustomerRepository;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomerRepository repo;

    public JwtFilter(JwtUtil jwtUtil, CustomerRepository repo) {
        this.jwtUtil = jwtUtil;
        this.repo = repo;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        String path = req.getRequestURI();

        if (path.startsWith("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            String email = null;
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                chain.doFilter(req, res);
                return;
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Customer c = repo.findByCorreo(email).orElse(null);

                if (c != null && jwtUtil.validateToken(token, email)) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    c,
                                    null,
                                    Collections.emptyList()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        chain.doFilter(req, res);
    }
}
