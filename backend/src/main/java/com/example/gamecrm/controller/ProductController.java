package com.example.gamecrm.controller;

import com.example.gamecrm.model.Product;
import com.example.gamecrm.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo){
        this.repo=repo;
    }

    @GetMapping
    public List<Product> all(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id){
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product create(@RequestBody Product p){
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product newP){
        return repo.findById(id).map(p -> {

            p.setNombre(newP.getNombre());
            p.setDescripcion(newP.getDescripcion());
            p.setStock(newP.getStock());
            p.setPrecio(newP.getPrecio());

            repo.save(p);
            return ResponseEntity.ok(p);

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
