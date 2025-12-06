package com.example.gamecrm.repository;

import com.example.gamecrm.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    

    Optional<Customer> findByCorreo(String correo);


    boolean existsByCorreo(String correo);
}