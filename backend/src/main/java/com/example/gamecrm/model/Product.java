package com.example.gamecrm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="store")
public class Product {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;

    public Product(){}

    public Product(String nombre, String descripcion, int stock, double precio){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id=id; }

    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre=nombre; }

    public String getDescripcion(){ return descripcion; }
    public void setDescripcion(String descripcion){ this.descripcion=descripcion; }

    public int getStock(){ return stock; }
    public void setStock(int stock){ this.stock=stock; }

    public double getPrecio(){ return precio; }
    public void setPrecio(double precio){ this.precio=precio; }
}
