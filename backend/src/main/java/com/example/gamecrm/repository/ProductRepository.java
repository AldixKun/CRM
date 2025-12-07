package com.example.gamecrm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.gamecrm.model.Product;

public interface ProductRepository extends MongoRepository<Product,String> {

}
