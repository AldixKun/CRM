package com.example.gamecrm.controller;

import com.example.gamecrm.dto.CheckoutRequest;
import com.example.gamecrm.model.Customer;
import com.example.gamecrm.model.Product;
import com.example.gamecrm.repository.CustomerRepository;
import com.example.gamecrm.repository.ProductRepository;
import com.example.gamecrm.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final JwtUtil jwtUtil;

    public CartController(ProductRepository productRepo, CustomerRepository customerRepo, JwtUtil jwtUtil) {
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/checkout")
    public Map<String, String> checkout(@RequestBody List<CheckoutRequest> items,
                                        @RequestHeader("Authorization") String tokenHeader) {
        
        System.out.println("--- INICIO CHECKOUT ---");
        try {
            String token = tokenHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            Customer customer = customerRepo.findByCorreo(email).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            double total = 0;
            StringBuilder resumen = new StringBuilder();

            for (CheckoutRequest item : items) {
                // Buscamos el producto por ID
                Optional<Product> pOpt = productRepo.findById(item.getProductId());
                
                if (pOpt.isPresent()) {
                    Product p = pOpt.get();
                    if (p.getStock() >= item.getQuantity()) {
                        p.setStock(p.getStock() - item.getQuantity());
                        productRepo.save(p);
                        
                        double subtotal = p.getPrecio() * item.getQuantity();
                        total += subtotal;
                        resumen.append(item.getQuantity()).append("x ").append(p.getNombre()).append(", ");
                    }
                }
            }

            // Guardamos la factura
            String factura = "{ \"fecha\": \"" + LocalDateTime.now() + "\", \"items\": \"" + resumen + "\", \"total\": " + total + " }";
            customer.getFacturas().add(factura);
            customer.setFacturasCount(customer.getFacturasCount() + 1);
            customerRepo.save(customer);

            System.out.println("--- COMPRA OK ---");
            return Map.of("status", "success");

        } catch (Exception e) {
            e.printStackTrace(); // ESTO IMPRIMIRÁ EL ERROR REAL EN CONSOLA
            throw new RuntimeException("Error en compra: " + e.getMessage());
        }
    }
}