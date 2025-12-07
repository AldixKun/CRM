package com.example.gamecrm.service;

import com.example.gamecrm.dto.CartItem;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    private ConcurrentHashMap<String, List<CartItem>> carts = new ConcurrentHashMap<>();

    public List<CartItem> getCart(String customerId){
        return carts.getOrDefault(customerId, new ArrayList<>());
    }

    public void addItem(String customerId, CartItem item){
        carts.compute(customerId, (k,v)->{

            if(v == null) v = new ArrayList<>();

            for(CartItem c : v){
                if(c.getProductId().equals(item.getProductId())){
                    c.setCantidad(c.getCantidad() + item.getCantidad());
                    return v;
                }
            }

            v.add(item);
            return v;
        });
    }

    public void removeItem(String customerId, String productId){
        carts.computeIfPresent(customerId,(k,v)->{
            v.removeIf(i -> i.getProductId().equals(productId));
            return v;
        });
    }

    public void clear(String customerId){
        carts.remove(customerId);
    }
}
