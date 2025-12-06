package com.example.gamecrm;

import com.example.gamecrm.model.Product;
import com.example.gamecrm.repository.ProductRepository;
import com.example.gamecrm.model.Customer;
import com.example.gamecrm.repository.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Configuration
public class DataLoader {

	@Bean
	CommandLineRunner initDB(ProductRepository productRepo, CustomerRepository customerRepo, PasswordEncoder encoder) {
	    return args -> {
	        // ¡LIMPIEZA TOTAL!
	        // productRepo.deleteAll();
	        // customerRepo.deleteAll();
      
            RestTemplate rt = new RestTemplate();
            String url = "https://www.freetogame.com/api/games";

            try {
                List<Map<String, Object>> juegos =
                    rt.getForObject(url, List.class);
                if (juegos != null) {
                    for (Map<String, Object> g : juegos) {
                        Product p = new Product();
                        p.setNombre((String) g.get("title"));
                        p.setDescripcion((String) g.getOrDefault("short_description", ""));
                        // No hay precio en la API asignamos un precio por defecto
                        p.setPrecio(19.99);
                        // No hay stock definimos stock por defecto
                        p.setStock(10);
                        productRepo.save(p);
                    }
                }
                System.out.println("Importación desde FreeToGame OK — juegos: " + productRepo.count());
            } catch (Exception e) {
                System.err.println("ERROR importando juegos desde FreeToGame");
                e.printStackTrace();
            }

            // Crear usuarios por defecto al inicio
            if (!customerRepo.existsByCorreo("admin@example.com")) {
                Customer admin = new Customer();
                admin.setNombre("Admin");
                admin.setCorreo("admin@example.com");
                admin.setPassword(encoder.encode("adminpass"));
                admin.setAdmin(true);
                admin.setFacturas(new ArrayList<>());
                admin.setFacturasCount(0);
                customerRepo.save(admin);
            }

            if (!customerRepo.existsByCorreo("ana@example.com")) {
                Customer user = new Customer();
                user.setNombre("Ana");
                user.setCorreo("ana@example.com");
                user.setPassword(encoder.encode("userpass"));
                user.setAdmin(false);
                user.setFacturas(new ArrayList<>());
                user.setFacturasCount(0);
                customerRepo.save(user);
            }
            
            if (!customerRepo.existsByCorreo("cliente_viejo@example.com")) {
                Customer vip = new Customer();
                vip.setNombre("Carlos El Historico");
                vip.setCorreo("cliente_viejo@example.com");
                vip.setPassword(encoder.encode("1234"));
                vip.setAdmin(false);
                vip.setAntiguedad(5); // 5 años
                vip.setDeudas(150.50); // Debe dinero
                
                // Factura 1 antigua
                String f1 = "{ \"fecha\": \"2023-05-20\", \"items\": \"1x Elden Ring, 1x FIFA 23\", \"total\": 120.00 }";
                // Factura 2 antigua
                String f2 = "{ \"fecha\": \"2024-01-10\", \"items\": \"2x Minecraft\", \"total\": 40.00 }";
                
                List<String> facturasVip = new ArrayList<>();
                facturasVip.add(f1);
                facturasVip.add(f2);
                
                vip.setFacturas(facturasVip);
                vip.setFacturasCount(2);
                
                customerRepo.save(vip);
            }

            System.out.println("Usuarios iniciales ok");
            
            
        };
    }
}
