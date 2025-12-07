package com.example.gamecrm.service;

import com.example.gamecrm.model.Product;
import com.example.gamecrm.model.Customer;
import com.example.gamecrm.repository.ProductRepository;
import com.example.gamecrm.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    public OrderService(CustomerRepository customerRepo, ProductRepository productRepo){
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    public Map<String,Object> checkout(String customerId, List<Map<String,Object>> carrito){

        Customer c = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        double total = 0;
        List<Map<String,Object>> detalle = new ArrayList<>();

        // procesar cada item
        for(Map<String,Object> item : carrito){

            String pid = item.get("productId").toString();
            int cantidad = Integer.parseInt(item.get("cantidad").toString());

            Product p = productRepo.findById(pid)
                    .orElseThrow(() -> new RuntimeException("Producto no existe"));

            if(p.getStock() < cantidad)
                throw new RuntimeException("Stock insuficiente de " + p.getNombre());

            double subtotal = p.getPrecio() * cantidad;
            total += subtotal;

            detalle.add(Map.of(
                    "nombre", p.getNombre(),
                    "precio", p.getPrecio(),
                    "cantidad", cantidad,
                    "subtotal", subtotal
            ));

            // Actualizar stock
            p.setStock(p.getStock() - cantidad);
            productRepo.save(p);
        }

        // Crear factura JSON
        Map<String,Object> factura = Map.of(
            "fecha", new Date().toString(),
            "cliente", c.getCorreo(),
            "detalle", detalle,
            "total", total
        );

        // Guardar factura
        c.getFacturas().add(factura.toString());
        c.setFacturasCount(c.getFacturasCount() + 1);
        customerRepo.save(c);

        return factura;
    }
}
