package com.example.gamecrm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection="crm")
public class Customer {

    @Id
    private String id;

    private String nombre;
    private String correo;
    private String password; // hashed
    private boolean isAdmin;
    private int antiguedad;
    private int facturasCount;
    private double deudas;

    private List<String> facturas = new ArrayList<>();

    public Customer(){}

    public Customer(String nombre, String correo, String passwordHash, boolean isAdmin, int antiguedad, double deudas){
        this.nombre = nombre;
        this.correo = correo;
        this.password = passwordHash;
        this.isAdmin = isAdmin;
        this.antiguedad = antiguedad;
        this.deudas = deudas;
        this.facturasCount = 0;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }

    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }

    public String getCorreo(){ return correo; }
    public void setCorreo(String correo){ this.correo = correo; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    public boolean isAdmin(){ return isAdmin; }
    public void setAdmin(boolean isAdmin){ this.isAdmin = isAdmin; }

    public int getAntiguedad(){ return antiguedad; }
    public void setAntiguedad(int antiguedad){ this.antiguedad = antiguedad; }

    public int getFacturasCount(){ return facturasCount; }
    public void setFacturasCount(int facturasCount){ this.facturasCount = facturasCount; }

    public double getDeudas(){ return deudas; }
    public void setDeudas(double deudas){ this.deudas = deudas; }

    public List<String> getFacturas(){ return facturas; }
    public void setFacturas(List<String> facturas){ this.facturas = facturas; }
}
