package com.example.gamecrm.controller;

import com.example.gamecrm.model.Customer;
import com.example.gamecrm.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"}) 
public class CustomerController {

    private final CustomerRepository repo;
    private final PasswordEncoder encoder;

    public CustomerController(CustomerRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping
    public List<Customer> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getOne(@PathVariable("id") String id) {
        List<Customer> todos = repo.findAll();
        for (Customer c : todos) {
            if (c.getId().toString().equals(id)) {
                return ResponseEntity.ok(c);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Customer create(@RequestBody Customer c) {
        if (c.getPassword() == null || c.getPassword().isEmpty()) c.setPassword("1234");
        c.setPassword(encoder.encode(c.getPassword()));
        if (c.getFacturas() == null) c.setFacturasCount(0);
        return repo.save(c); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Map<String, Object> datos) {
        try {
            Customer existing = null;
            List<Customer> todos = repo.findAll();
            for (Customer temp : todos) {
                if (temp.getId().toString().equals(id)) {
                    existing = temp;
                    break;
                }
            }

            if (existing != null) {
                if (datos.containsKey("nombre")) existing.setNombre((String) datos.get("nombre"));
                if (datos.containsKey("correo")) existing.setCorreo((String) datos.get("correo"));
                
                if (datos.containsKey("antiguedad")) {
                    Object val = datos.get("antiguedad");
                    if (val instanceof Number) existing.setAntiguedad(((Number) val).intValue());
                    else if (val instanceof String) existing.setAntiguedad(Integer.parseInt((String) val));
                }

                repo.save(existing);
                return ResponseEntity.ok(existing);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        repo.deleteById(id);
    }
}